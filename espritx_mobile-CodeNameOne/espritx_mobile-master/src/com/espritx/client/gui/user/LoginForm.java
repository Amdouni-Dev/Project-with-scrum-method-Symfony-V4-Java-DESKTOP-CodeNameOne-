/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.user;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ToastBar;
import com.codename1.fingerprint.Fingerprint;
import com.codename1.io.Log;
import com.codename1.social.GoogleConnect;
import com.codename1.social.Login;
import com.codename1.social.LoginCallback;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.codename1.uikit.pheonixui.InboxForm;
import com.codename1.uikit.pheonixui.SplashForm;
import com.espritx.client.services.User.AuthenticationService;
import com.espritx.client.utils.Statics;
import com.espritx.client.utils.StringUtils;

/**
 * @author Wahib
 */
public class LoginForm extends BaseForm {

    public static final String CREDENTIAL_SEPERATOR = ";/////;";
    private static final String authenticationBucket = "credentials";


    public LoginForm() {
        this(Resources.getGlobalResources());
    }

    public LoginForm(Resources resourceObjectInstance) {
        setInlineStylesTheme(resourceObjectInstance);
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setTitle("Sign in");
        setName("SignInForm");
        initUserControls(resourceObjectInstance);
        getTitleArea().setUIID("Container");
        getToolbar().setUIID("Container");
        getToolbar().getTitleComponent().setUIID("SigninTitle");
        getContentPane().setUIID("SignInForm");
    }

    private void initUserControls(Resources resourceObjectInstance) {
        Container formContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        formContainer.setScrollableY(true);
        formContainer.setInlineStylesTheme(resourceObjectInstance);
        addComponent(BorderLayout.CENTER, formContainer);

        Label profilePicture = new Label();
        profilePicture.setUIID("CenterLabel");
        profilePicture.setInlineStylesTheme(resourceObjectInstance);
        profilePicture.setIcon(resourceObjectInstance.getImage("logo.png"));
        formContainer.addComponent(profilePicture);

        ComponentGroup credentialsContainer = makeComponentGroup("CredentialsContainer");
        TextField emailTextField = makeTextField("EmailTextField", "@esprit.tn e-mail", "test_super_admin_618@esprit.tn");
        emailTextField.setConstraint(TextField.EMAILADDR);
        credentialsContainer.addComponent(emailTextField);
        TextField passwordTextField = makeTextField("PasswordTextField", "Password", "12345");
        passwordTextField.setConstraint(TextField.PASSWORD);
        credentialsContainer.addComponent(passwordTextField);
        formContainer.addComponent(credentialsContainer);
        formContainer.add(BoxLayout.encloseXCenter(new CheckBox("Remember me?")));

        /* Biometric Login **/
        CheckBox save_account_to_device = new CheckBox("Save to device");
        save_account_to_device.setSelected(false);
        if (Fingerprint.isAvailable()) {
            formContainer.add(save_account_to_device);
            FloatingActionButton fab = FloatingActionButton.createBadge("Biometric Login");
            formContainer.add(fab);
            fab.addActionListener(evt -> {
                Fingerprint.getPassword(
                        "Get account from device",  // Message to display in auth dialog
                        authenticationBucket
                ).onResult((password, err) -> {
                    if (err != null) {
                        Log.e(err);
                        Log.p("Failed to get password: " + err.getMessage());
                        ToastBar.showErrorMessage("Failed to get password: " + err.getMessage());
                    } else {
                        Log.p("The password was " + password);
                        String[] fragments = StringUtils.split(password, CREDENTIAL_SEPERATOR);
                        Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
                        try {
                            AuthenticationService.Authenticate(fragments[0], fragments[1]);
                            dlg.dispose();
                            new InboxForm(resourceObjectInstance).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dlg.dispose();
                        }
                    }
                });
            });
        }
        /* end biometric login */

        /* Start login button */
        Button loginButton = makeButton("SignInButton", "Sign In");
        loginButton.addActionListener(evt -> {
            Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
            try {
                String email = emailTextField.getText();
                String password = passwordTextField.getText();
                String token = email + CREDENTIAL_SEPERATOR + password;
                AuthenticationService.Authenticate(email, password);
                dlg.dispose();
                if (save_account_to_device.isSelected()) {
                    Fingerprint.addPassword(
                            "Save account to device", // Message to display in authentication dialog
                            authenticationBucket,
                            token
                    ).onResult((success, err) -> {
                        if (err != null) {
                            Log.e(err);
                            ToastBar.showErrorMessage("Failed to save password: " + err.getMessage());
                        } else {
                            ToastBar.showInfoMessage("Successfully saved password.");
                        }
                    });
                }

                new InboxForm().show();
            } catch (Exception e) {
                Log.p(e.getMessage(), Log.ERROR);
                dlg.dispose();
                Dialog.show("error", e.getMessage(), "ok", null);
            }
        });
        formContainer.addComponent(loginButton);
        /* end login button */

        /* start Google Login */
        Button loginwg = makeButton("googleSignin", "Single Sign On");
        loginwg.addActionListener((evt -> {
            AuthenticationService.StartDeviceAuthrorization();
        }));
        formContainer.addComponent(loginwg);


        //formContainer.addComponent(makeButton("ForgotPasswordContainer", "Forgot your Password?", "CenterLabelSmall"));
    }
}
