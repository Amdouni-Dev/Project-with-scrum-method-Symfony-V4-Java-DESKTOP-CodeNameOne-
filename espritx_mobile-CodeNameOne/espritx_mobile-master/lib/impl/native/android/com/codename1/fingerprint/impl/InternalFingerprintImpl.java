/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Codename One through http://www.codenameone.com/ if you 
 * need additional information or have any questions.
 */
package com.codename1.fingerprint.impl;

import android.hardware.fingerprint.FingerprintManager;
import com.codename1.impl.android.AndroidNativeUtil;
import com.codename1.impl.android.AndroidImplementation;

import android.os.Build;
import android.os.CancellationSignal;
import android.hardware.fingerprint.FingerprintManager.AuthenticationCallback;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.Looper;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import com.codename1.io.Log;
import com.codename1.ui.CN;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class InternalFingerprintImpl {
    private FingerprintManager mFingerPrintManager;
    private BiometricManager mBiometricManager;
    private KeyGenerator mKeyGenerator;
    private static final String KEY_ID = "FingerprintScannerKey";
    private static final String SHARED_PREFS_NAME = "FingerprintScannerPrefs";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private KeyStore mKeyStore;
    private Cipher mCipher;
    private static CancellationSignal cancellationSignal;
    
    
       
    public boolean isAvailable() {
        //final boolean[] response = new boolean[1];
        if (android.os.Build.VERSION.SDK_INT < 23) {
            return false;
        }
        final StringBuilder paramBuilder = new StringBuilder();
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            CN.setProperty("FingerprintScanner.showDialogOnAndroid", "false");
            AndroidImplementation.runOnUiThreadAndBlock(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                public void run() {
                    if(!AndroidNativeUtil.checkForPermission(Manifest.permission.USE_BIOMETRIC, "Authorize using biometrics")){
                        return;
                    }
                    try {
                        if (mBiometricManager == null) {
                            mBiometricManager = (BiometricManager)AndroidNativeUtil.getActivity().
                                                                                                    getSystemService(Activity.BIOMETRIC_SERVICE);
                        }
                        PackageManager packageManager = AndroidNativeUtil.getActivity().getPackageManager();
                        boolean canAuthenticate = mBiometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
                        if (canAuthenticate) {
                            if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                                FingerprintManager fingerprintManager = (FingerprintManager)AndroidNativeUtil.getActivity().getSystemService(Activity.FINGERPRINT_SERVICE);
                                if (fingerprintManager.hasEnrolledFingerprints()) {
                                    paramBuilder.append("touch ");
                                }
                            }
                            if (packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)) {
                                paramBuilder.append("face ");
                            }
                            if (packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)) {
                                paramBuilder.append("iris ");
                            }
                        }

                        
                    } catch(Throwable t) {
                        Log.p("This exception could be 100% valid on old devices, we're logging it just to be safe. Older devices might throw NoClassDefFoundError...");
                        Log.e(t);
                    }
                }
            });
        } else {
            CN.setProperty("FingerprintScanner.showDialogOnAndroid", "true");
            AndroidImplementation.runOnUiThreadAndBlock(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                public void run() {
                    if(!AndroidNativeUtil.checkForPermission(Manifest.permission.USE_FINGERPRINT, "Authorize using fingerprint")){
                        return;
                    }
                    try {
                        mFingerPrintManager = (FingerprintManager)AndroidNativeUtil.getActivity().
                                                                                                getSystemService(Activity.FINGERPRINT_SERVICE);

                        if (mFingerPrintManager.isHardwareDetected() && 
                                mFingerPrintManager.hasEnrolledFingerprints()) {
                            paramBuilder.append("touch ");
                        }
                    } catch(Throwable t) {
                        Log.p("This exception could be 100% valid on old devices, we're logging it just to be safe. Older devices might throw NoClassDefFoundError...");
                        Log.e(t);
                    }
                }
            });
        }
        String param = paramBuilder.toString().trim();
        CN.setProperty("Fingerprint.types", param);
        
        return !param.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void scan29(final String reason) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            public void run() {
                if (cancellationSignal != null) {
                    // Fingerprint scanning can continue for a long time even after the user
                    // has given up.  Keep a reference globally so that we can clear it
                    // if we need to start another scan.
                    cancellationSignal.cancel();
                    cancellationSignal = null;
                }
                final CancellationSignal cs = new CancellationSignal();
                cancellationSignal = cs;
                
                BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
                    int failures;
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        InternalCallback.scanFail();
                    }

                    public void onAuthenticationFailed() {
                        // NOTE: Often authentication fails several times
                            // before it succeeds.  Because often times it takes a while
                            // for the user to put their finger in the right spot
                            // This method will be called continually in a stream until
                            // either the user cancels the action, it succeeds, or until
                            // we cancel the action with cs.cancel.
                            // Here, we'll try 5 times then quit.
                        if (failures++ > 5) {
                            cs.cancel();
                            InternalCallback.scanFail();
                        }
                    }
                    
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        cs.cancel();
                        InternalCallback.scanSuccess();
                    }
                };
                new BiometricPrompt.Builder(AndroidNativeUtil.getActivity())
                        .setTitle(reason)
                        .setNegativeButton("Cancel", AndroidNativeUtil.getActivity().getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InternalCallback.scanFail();
                    }
                })
                .build().authenticate(cs, AndroidNativeUtil.getActivity().getMainExecutor(), callback);
                
            }
        });
    }
    
    public void scan(String reason) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            Log.p("minimum SDK version 23 required");
            InternalCallback.scanFail();
        }
        
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            scan29(reason);
            return;
        }
        
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                if (cancellationSignal != null) {
                    // Fingerprint scanning can continue for a long time even after the user
                    // has given up.  Keep a reference globally so that we can clear it
                    // if we need to start another scan.
                    cancellationSignal.cancel();
                    cancellationSignal = null;
                }
                final CancellationSignal cs = new CancellationSignal();
                cancellationSignal = cs;
                
                AuthenticationCallback callback = new AuthenticationCallback() {
                    int failures;
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        InternalCallback.scanFail();
                    }

                    public void onAuthenticationFailed() {
                        // NOTE: Often authentication fails several times
                            // before it succeeds.  Because often times it takes a while
                            // for the user to put their finger in the right spot
                            // This method will be called continually in a stream until
                            // either the user cancels the action, it succeeds, or until
                            // we cancel the action with cs.cancel.
                            // Here, we'll try 5 times then quit.
                        if (failures++ > 5) {
                            cs.cancel();
                            InternalCallback.scanFail();
                        }
                    }
                    
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        cs.cancel();
                        InternalCallback.scanSuccess();
                    }
                };
                
                mFingerPrintManager.authenticate(null, cs, 0, callback, null);
            }
        });
    }

    public boolean isSupported() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isFingerprintAuthAvailable() {
        
        if (Looper.getMainLooper().isCurrentThread()) {
            if(!AndroidNativeUtil.checkForPermission(Manifest.permission.USE_FINGERPRINT, "Authorize using fingerprint")){
                    return false;
                }
                try {
                    mFingerPrintManager = (FingerprintManager)AndroidNativeUtil.getActivity().
                                                                                            getSystemService(Activity.FINGERPRINT_SERVICE);

                    return mFingerPrintManager.isHardwareDetected() && 
                        mFingerPrintManager.hasEnrolledFingerprints();
                } catch(Throwable t) {
                    Log.p("This exception could be 100% valid on old devices, we're logging it just to be safe. Older devices might throw NoClassDefFoundError...");
                    Log.e(t);
                    return false;
                }
        } else {
            return isAvailable();
        }
    }
    
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean isBiometricAuthAvailable() {
        
        if (Looper.getMainLooper().isCurrentThread()) {
            if(!AndroidNativeUtil.checkForPermission(Manifest.permission.USE_FINGERPRINT, "Authorize using fingerprint")){
                    return false;
                }
                try {
                    if (mBiometricManager == null) {
                        mBiometricManager = (BiometricManager)AndroidNativeUtil.getActivity().
                                                                                            getSystemService(Activity.BIOMETRIC_SERVICE);
                    }

                    return mBiometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
                } catch(Throwable t) {
                    Log.p("This exception could be 100% valid on old devices, we're logging it just to be safe. Older devices might throw NoClassDefFoundError...");
                    Log.e(t);
                    return false;
                }
        } else {
            return isAvailable();
        }
    }
    
    
    public void addPassword(int requestId, String reason, String key, String value) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            Log.p("minimum SDK version 23 required");
            InternalCallback.requestError(requestId, "minimum SDK version 23 is required");
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            addPassword29(requestId, reason, key, value, true, 0);
        } else {
            addPassword(requestId, reason, key, value, true, 0);
        }
    }
    
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean addPassword29(final int requestId, final String reason, final String account, final String password, final boolean requestAuth, final int depth) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            Log.p("minimum SDK version 23 required");
            InternalCallback.requestError(requestId, "minimum SDK version 23 is required");
            return true;
        }
        final SharedPreferences sharedPref = AndroidNativeUtil.getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        if (isBiometricAuthAvailable()) {
            SecretKey secretKey = getSecretKey();

            if (secretKey == null) {
                if (createKey()) {
                    secretKey = getSecretKey();
                } else {
                    InternalCallback.requestError(requestId, String.valueOf("Failed to create key"));
                    return false;
                }
            }




            AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (cancellationSignal != null) {
                        // Fingerprint scanning can continue for a long time even after the user
                        // has given up.  Keep a reference globally so that we can clear it
                        // if we need to start another scan.
                        cancellationSignal.cancel();
                        cancellationSignal = null;
                    }
                    final CancellationSignal cs = new CancellationSignal();
                    cancellationSignal = cs;
                    BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
                        int failures;
                        public void onAuthenticationError(int errorCode, CharSequence errString) {

                            InternalCallback.requestError(requestId, String.valueOf(errString));
                        }

                        
                        public void onAuthenticationFailed() {
                            // NOTE: Often authentication fails several times
                            // before it succeeds.  Because often times it takes a while
                            // for the user to put their finger in the right spot
                            // This method will be called continually in a stream until
                            // either the user cancels the action, it succeeds, or until
                            // we cancel the action with cs.cancel.
                            // Here, we'll try 5 times then quit.
                            //if (failures++ > 5) {
                            //    cs.cancel();
                            //    InternalCallback.requestComplete(requestId, false);
                            //}
                        }

                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            cs.cancel();
                            byte[] enc = new byte[0];
                            try {
                                Cipher cipher = result.getCryptoObject().getCipher();
                                enc = cipher.doFinal(password.getBytes());

                                editor.putString("fing" + account, Base64.encodeToString(enc, Base64.DEFAULT));
                                editor.putString("fing_iv" + account,
                                        Base64.encodeToString(cipher.getIV(), Base64.DEFAULT));

                                editor.apply();
                                InternalCallback.requestComplete(requestId, true);
                            } catch (Throwable e) {
                                if (depth == 0) {
                                    // This typically shouldn't happen.  If the key was invalidated, it should have caught
                                    // it before the authentication step in initCipher.  BUT in some cases, e.g. Samsung
                                    // phones on 8.0.0, it somehow passes that initial step without failure, but fails
                                    // here.
                                    //https://issuetracker.google.com/u/0/issues/65578763
                                    removePermanentlyInvalidatedKey();
                                    addPassword29(requestId, reason, account, password, requestAuth, depth + 1);
                                } else {


                                    Log.e(e);
                                    InternalCallback.requestError(requestId, e.getMessage());
                                }
                            }

                        }
                    };
                    if(!initCipher(Cipher.ENCRYPT_MODE, account)) {
                        // Could not initialize cipher, key must have been invalidated
                        if (!createKey()) {
                            InternalCallback.requestError(requestId, "Failed to create a new key after old key failed to initialize the cipher.  Something must be wrong.");
                            return;
                        }
                        if(!initCipher(Cipher.ENCRYPT_MODE, account)) {
                            InternalCallback.requestError(requestId, "Failed to initialize the cipher even after generating new key.  Something must be wrong");
                            return;
                        }


                    }
                    BiometricPrompt.CryptoObject crypto = new BiometricPrompt.CryptoObject(mCipher());
                    
                    //mBiometricManager.authenticate(crypto, cs, 0, callback, null);
                   new BiometricPrompt.Builder(AndroidNativeUtil.getActivity())
                        .setTitle(reason)
                        .setNegativeButton("Cancel", AndroidNativeUtil.getActivity().getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InternalCallback.requestError(requestId, "__CANCELLED__");
                    }
                })
                .build().authenticate(crypto, cs, AndroidNativeUtil.getActivity().getMainExecutor(), callback);
                            
                }
            });

        } else {
            InternalCallback.requestError(requestId, String.valueOf("No biometric hardware is available"));

        }
        return true;
        
    }
    /**
     * Adds a secure item.
     * @param requestId The request ID
     * @param reason The reason to show if a biometric prompt is shown.
     * @param account The account name
     * @param password The password
     * @param requestAuth Whether to request biometric authentication.
     * @return If requestAuth is false, and we *need* to use biometric authentication, then this will return false.  Otherwise it returns true.
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPassword(final int requestId, final String reason, final String account, final String password, final boolean requestAuth, final int depth) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            Log.p("minimum SDK version 23 required");
            InternalCallback.requestError(requestId, "minimum SDK version 23 is required");
            return true;
        }
        final SharedPreferences sharedPref = AndroidNativeUtil.getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        if (isFingerprintAuthAvailable()) {
            SecretKey secretKey = getSecretKey();

            if (secretKey == null) {
                if (createKey()) {
                    secretKey = getSecretKey();
                } else {
                    InternalCallback.requestError(requestId, String.valueOf("Failed to create key"));
                    return false;
                }
            }




            AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (cancellationSignal != null) {
                        // Fingerprint scanning can continue for a long time even after the user
                        // has given up.  Keep a reference globally so that we can clear it
                        // if we need to start another scan.
                        cancellationSignal.cancel();
                        cancellationSignal = null;
                    }
                    final CancellationSignal cs = new CancellationSignal();
                    cancellationSignal = cs;
                    AuthenticationCallback callback = new AuthenticationCallback() {
                        int failures;
                        public void onAuthenticationError(int errorCode, CharSequence errString) {

                            InternalCallback.requestError(requestId, String.valueOf(errString));
                        }

                        
                        public void onAuthenticationFailed() {
                            // NOTE: Often authentication fails several times
                            // before it succeeds.  Because often times it takes a while
                            // for the user to put their finger in the right spot
                            // This method will be called continually in a stream until
                            // either the user cancels the action, it succeeds, or until
                            // we cancel the action with cs.cancel.
                            // Here, we'll try 5 times then quit.
                            //if (failures++ > 5) {
                            //    cs.cancel();
                            //    InternalCallback.requestComplete(requestId, false);
                            //}
                        }

                        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                            cs.cancel();
                            byte[] enc = new byte[0];
                            try {
                                Cipher cipher = result.getCryptoObject().getCipher();
                                enc = cipher.doFinal(password.getBytes());

                                editor.putString("fing" + account, Base64.encodeToString(enc, Base64.DEFAULT));
                                editor.putString("fing_iv" + account,
                                        Base64.encodeToString(cipher.getIV(), Base64.DEFAULT));

                                editor.apply();
                                InternalCallback.requestComplete(requestId, true);
                            } catch (Throwable e) {
                                if (depth == 0) {
                                    // This typically shouldn't happen.  If the key was invalidated, it should have caught
                                    // it before the authentication step in initCipher.  BUT in some cases, e.g. Samsung
                                    // phones on 8.0.0, it somehow passes that initial step without failure, but fails
                                    // here.
                                    //https://issuetracker.google.com/u/0/issues/65578763
                                    removePermanentlyInvalidatedKey();
                                    addPassword(requestId, reason, account, password, requestAuth, depth + 1);
                                } else {


                                    Log.e(e);
                                    InternalCallback.requestError(requestId, e.getMessage());
                                }
                            }

                        }
                    };
                    if(!initCipher(Cipher.ENCRYPT_MODE, account)) {
                        // Could not initialize cipher, key must have been invalidated
                        if (!createKey()) {
                            InternalCallback.requestError(requestId, "Failed to create a new key after old key failed to initialize the cipher.  Something must be wrong.");
                            return;
                        }
                        if(!initCipher(Cipher.ENCRYPT_MODE, account)) {
                            InternalCallback.requestError(requestId, "Failed to initialize the cipher even after generating new key.  Something must be wrong");
                            return;
                        }


                    }
                    CryptoObject crypto = new CryptoObject(mCipher());
                    mFingerPrintManager.authenticate(crypto, cs, 0, callback, null);
                }
            });

        } else {
            InternalCallback.requestError(requestId, String.valueOf("No biometric hardware is available"));

        }
        return true;
        
    }
    public void deletePassword(int requestId, String reason, String account) {
        SharedPreferences sharedPref = AndroidNativeUtil.getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        sharedPref.edit().remove("fing"+account).remove("fing_iv"+account).apply();
        InternalCallback.requestComplete(requestId, true);
    }

    public void getPassword(int requestId, String reason, String key) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            Log.p("minimum SDK version 23 required");
            InternalCallback.requestError(requestId, "minimum SDK version 23 is required");
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getPassword29(requestId, reason, key, true);
        } else {
            getPassword(requestId, reason, key, true);
        }
    }
    
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean getPassword29(final int requestId, final String reason, final String key, final boolean requestAuth) {

        final SharedPreferences sharedPref = AndroidNativeUtil.getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        if (sharedPref.getString("fing_iv" + key, null) == null) {
            InternalCallback.requestError(requestId,"The specified item could not be found in the keychain.");
            return false;
        }
        if (isBiometricAuthAvailable()) {
            SecretKey secretKey = getSecretKey();

            if (secretKey == null) {
                /*
                if (createKey()) {
                    secretKey = getSecretKey();
                } else {
                    InternalCallback.requestError(requestId, String.valueOf("Failed to create key"));
                    return false;
                }
                */
                if (keyRevoked) {
                    InternalCallback.requestKeyRevokedError(requestId, String.valueOf("Key has been invalidated"));
                } else {
                    InternalCallback.requestError(requestId, String.valueOf("No key found"));
                }
                return false;
            }


            
            AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                public void run() {
                    // Fingerprint scanning can continue for a long time even after the user
                    // has given up.  Keep a reference globally so that we can clear it
                    // if we need to start another scan.
                    if (cancellationSignal != null) {
                        cancellationSignal.cancel();
                        cancellationSignal = null;
                    }
                    final CancellationSignal cs = new CancellationSignal();
                    cancellationSignal = cs;
                    BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
                        int failures;

                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            InternalCallback.requestError(requestId, String.valueOf(errString));
                        }

                        public void onAuthenticationFailed() {
                            // NOTE: Often authentication fails several times
                            // before it succeeds.  Because often times it takes a while
                            // for the user to put their finger in the right spot
                            // This method will be called continually in a stream until
                            // either the user cancels the action, it succeeds, or until
                            // we cancel the action with cs.cancel.
                            // Here, we'll try 5 times then quit.
                            //if (failures++ > 5) {
                            //    cs.cancel();
                            //    InternalCallback.requestError(requestId, "Authentication failed");
                            //}

                        }

                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            cs.cancel();
                            Cipher cipher = result.getCryptoObject().getCipher();


                            try {
                                byte[] enc = Base64.decode(sharedPref.getString("fing" + key, ""), Base64.DEFAULT);

                                byte[] decrypted = cipher.doFinal(enc);
                                String decryptedStr = new String(decrypted);
                                InternalCallback.requestSuccess(requestId, decryptedStr);
                            } catch (Throwable e) {

                                Log.e(e);
                                InternalCallback.requestError(requestId, e.getMessage());
                            }


                        }
                    };
                    if(!initCipher(Cipher.DECRYPT_MODE, key)) {
                        // Could not initialize cipher, key must have been invalidated
                        InternalCallback.requestKeyRevokedError(requestId, "Failed to initialize cipher.  Secret key must have been revoked");
                        return;
                        /*
                        if (!createKey()) {
                            InternalCallback.requestError(requestId, "Failed to create a new key after old key failed to initialize the cipher.  Something must be wrong.");
                            return;
                        }
                        if(!initCipher(Cipher.DECRYPT_MODE, key)) {
                            InternalCallback.requestError(requestId, "Failed to initialize the cipher even after generating new key.  Something must be wrong");
                            return;
                        }
                        */


                    }

                    BiometricPrompt.CryptoObject crypto = new BiometricPrompt.CryptoObject(mCipher());
                    //mFingerPrintManager.authenticate(crypto, cs, 0, callback, null);
                    new BiometricPrompt.Builder(AndroidNativeUtil.getActivity())
                        .setTitle(reason)
                        .setNegativeButton("Cancel", AndroidNativeUtil.getActivity().getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InternalCallback.requestError(requestId, "__CANCELLED__");
                    }
                })
                .build().authenticate(crypto, cs, AndroidNativeUtil.getActivity().getMainExecutor(), callback);
                }
            });

        } else {
            InternalCallback.requestError(requestId, String.valueOf("No biometric hardware is available"));

        }
        return true;
        
    }

    /**
     * Gets a secure item.
     * @param requestId The request ID
     * @param reason The reason to show if a biometric prompt is shown.
     * @param key The key of the secure item.
     * @param requestAuth Whether to request biometric authentication.
     * @return If requestAuth is false, and we *need* to use biometric authentication, then this will return false.  Otherwise it returns true.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean getPassword(final int requestId, final String reason, final String key, final boolean requestAuth) {

        final SharedPreferences sharedPref = AndroidNativeUtil.getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        if (sharedPref.getString("fing_iv" + key, null) == null) {
            InternalCallback.requestError(requestId,"The specified item could not be found in the keychain.");
            return false;
        }
        if (isFingerprintAuthAvailable()) {
            SecretKey secretKey = getSecretKey();

            if (secretKey == null) {
                /*
                if (createKey()) {
                    secretKey = getSecretKey();
                } else {
                    InternalCallback.requestError(requestId, String.valueOf("Failed to create key"));
                    return false;
                }
                */
                if (keyRevoked) {
                    InternalCallback.requestKeyRevokedError(requestId, String.valueOf("Key has been invalidated"));
                } else {
                    InternalCallback.requestError(requestId, String.valueOf("No key found"));
                }
                return false;
            }


            
            AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                public void run() {
                    // Fingerprint scanning can continue for a long time even after the user
                    // has given up.  Keep a reference globally so that we can clear it
                    // if we need to start another scan.
                    if (cancellationSignal != null) {
                        cancellationSignal.cancel();
                        cancellationSignal = null;
                    }
                    final CancellationSignal cs = new CancellationSignal();
                    cancellationSignal = cs;
                    AuthenticationCallback callback = new AuthenticationCallback() {
                        int failures;

                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            InternalCallback.requestError(requestId, String.valueOf(errString));
                        }

                        public void onAuthenticationFailed() {
                            // NOTE: Often authentication fails several times
                            // before it succeeds.  Because often times it takes a while
                            // for the user to put their finger in the right spot
                            // This method will be called continually in a stream until
                            // either the user cancels the action, it succeeds, or until
                            // we cancel the action with cs.cancel.
                            // Here, we'll try 5 times then quit.
                            //if (failures++ > 5) {
                            //    cs.cancel();
                            //    InternalCallback.requestError(requestId, "Authentication failed");
                            //}

                        }

                        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                            cs.cancel();
                            Cipher cipher = result.getCryptoObject().getCipher();


                            try {
                                byte[] enc = Base64.decode(sharedPref.getString("fing" + key, ""), Base64.DEFAULT);

                                byte[] decrypted = cipher.doFinal(enc);
                                String decryptedStr = new String(decrypted);
                                InternalCallback.requestSuccess(requestId, decryptedStr);
                            } catch (Throwable e) {

                                Log.e(e);
                                InternalCallback.requestError(requestId, e.getMessage());
                            }


                        }
                    };
                    if(!initCipher(Cipher.DECRYPT_MODE, key)) {
                        // Could not initialize cipher, key must have been invalidated
                        InternalCallback.requestKeyRevokedError(requestId, "Failed to initialize cipher.  Secret key must have been revoked");
                        return;
                        /*
                        if (!createKey()) {
                            InternalCallback.requestError(requestId, "Failed to create a new key after old key failed to initialize the cipher.  Something must be wrong.");
                            return;
                        }
                        if(!initCipher(Cipher.DECRYPT_MODE, key)) {
                            InternalCallback.requestError(requestId, "Failed to initialize the cipher even after generating new key.  Something must be wrong");
                            return;
                        }
                        */


                    }

                    CryptoObject crypto = new CryptoObject(mCipher());
                    mFingerPrintManager.authenticate(crypto, cs, 0, callback, null);
                }
            });

        } else {
            InternalCallback.requestError(requestId, String.valueOf("No biometric hardware is available"));

        }
        return true;
        
    }
    
    private KeyStore mKeyStore() {
        if (mKeyStore == null) {
            try {
                mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
                mKeyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
            } catch (NoSuchProviderException e) {
                throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
            } catch (KeyStoreException e) {
                throw new RuntimeException("Failed to get an instance of KeyStore", e);
            }
        }
        return mKeyStore;
    }
    
    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean createKey() {
        String errorMessage = "";
        String createKeyExceptionErrorPrefix = "Failed to create key: ";
        boolean isKeyCreated = false;
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore().load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            mKeyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ID,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(
                    KeyProperties.BLOCK_MODE_CBC)

                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeyGenerator.generateKey();

            isKeyCreated = true;
        } catch (NoSuchAlgorithmException e) {
            errorMessage = createKeyExceptionErrorPrefix + "NoSuchAlgorithmException";
        } catch (InvalidAlgorithmParameterException e) {
            errorMessage = createKeyExceptionErrorPrefix + "InvalidAlgorithmParameterException";
        } catch (CertificateException e) {
            errorMessage = createKeyExceptionErrorPrefix + "CertificateException";
        } catch (IOException e) {
            errorMessage = createKeyExceptionErrorPrefix + "IOException";
        }
        if (!isKeyCreated) {
            Log.p(errorMessage);
            
        }
        return isKeyCreated;
    }
     private boolean keyRevoked = false;
     private SecretKey getSecretKey() {
         keyRevoked = false;
        String errorMessage = "";
        String getSecretKeyExceptionErrorPrefix = "Failed to get SecretKey from KeyStore: ";
        SecretKey key = null;
        try {
            mKeyStore().load(null);
            key = (SecretKey) mKeyStore.getKey(KEY_ID, null);
        } catch (KeyStoreException e) {
            errorMessage = getSecretKeyExceptionErrorPrefix + "KeyStoreException";
        } catch (CertificateException e) {
            errorMessage = getSecretKeyExceptionErrorPrefix + "CertificateException";
        } catch (UnrecoverableKeyException e) {
            keyRevoked = true;
            errorMessage = getSecretKeyExceptionErrorPrefix + "UnrecoverableKeyException";
        } catch (IOException e) {
            errorMessage = getSecretKeyExceptionErrorPrefix + "IOException";
        } catch (NoSuchAlgorithmException e) {
            errorMessage = getSecretKeyExceptionErrorPrefix + "NoSuchAlgorithmException";
        } catch (UnrecoverableEntryException e) {
            keyRevoked = true;
            errorMessage = getSecretKeyExceptionErrorPrefix + "UnrecoverableEntryException";
        }
        if (key == null) {
            Log.p(errorMessage);
        }
        return key;
    }
    
    private Cipher mCipher() {
        if (mCipher == null) {
            try {
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
                        + "/"
                        + KeyProperties.BLOCK_MODE_CBC
                        + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed to get an instance of Cipher", e);
            } catch (NoSuchPaddingException e) {
                throw new RuntimeException("Failed to get an instance of Cipher", e);
            }

        }
        return mCipher;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static class CipherInitializerM {


        @RequiresApi(api = Build.VERSION_CODES.M)
        private static boolean initCipher(InternalFingerprintImpl impl, int mode, String account) {
            
            boolean initCipher = false;
            String errorMessage = "";
            String initCipherExceptionErrorPrefix = "Failed to init Cipher: ";
            try {
                SecretKey key = impl.getSecretKey();

                if (mode == Cipher.ENCRYPT_MODE) {
                    SecureRandom r = new SecureRandom();
                    byte[] ivBytes = new byte[16];
                    r.nextBytes(ivBytes);

                    impl.mCipher().init(mode, key);
                } else {
                    SharedPreferences sharedPref = AndroidNativeUtil.getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
                    byte[] ivBytes =
                            Base64.decode(sharedPref.getString("fing_iv" + account, ""), Base64.DEFAULT);
                    impl.mCipher().init(mode, key, new IvParameterSpec(ivBytes));
                }

                initCipher = true;
            } catch (KeyPermanentlyInvalidatedException e) {
                // Note: on Samsung devices with 8.0.0 this doesn't work... it will initially work
                // and then fail after authentication saying that user was not authenticated.
                // https://issuetracker.google.com/u/0/issues/65578763
                // We'll handle that additional failure there.
                impl.removePermanentlyInvalidatedKey();
                errorMessage = "KeyPermanentlyInvalidatedException";

            } catch (InvalidKeyException e) {
                errorMessage = initCipherExceptionErrorPrefix + "InvalidKeyException";
            } catch (InvalidAlgorithmParameterException e) {
                errorMessage = initCipherExceptionErrorPrefix + "InvalidAlgorithmParameterException";
                e.printStackTrace();
            }
            if (!initCipher) {
                Log.p(errorMessage);
            }
            return initCipher;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean initCipher(int mode, String account) {
        return CipherInitializerM.initCipher(this, mode, account);
    }

    private void removePermanentlyInvalidatedKey() {
        try {
            mKeyStore().deleteEntry(KEY_ID);
            Log.p("Permanently invalidated key was removed.");
        } catch (KeyStoreException e) {
            Log.e(e);
        }
    }
    
    public void cancelRequest(final int requestId) {
         AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (cancellationSignal != null) {
                    cancellationSignal.cancel();
                    cancellationSignal = null;
                    InternalCallback.requestError(requestId, "__CANCELLED__");
                }
                
            }
         });
    }
}
