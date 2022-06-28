package com.codename1.fingerprint.impl;

import com.codename1.ui.Display;
import java.util.Map;

public class InternalFingerprintImpl implements com.codename1.fingerprint.impl.InternalFingerprint{
    
    
    private boolean faceIDUsageDescriptionChecked;
    
    private void checkFaceIDUsageDescription() {
        if (!faceIDUsageDescriptionChecked) {
            faceIDUsageDescriptionChecked = true;
            
            Map<String, String> m = Display.getInstance().getProjectBuildHints();
            if(m != null) {
                if(!m.containsKey("ios.NSFaceIDUsageDescription")) {
                    Display.getInstance().setProjectBuildHint("ios.NSFaceIDUsageDescription", "Some functionality of the application requires FaceID functionality");
                }
            }
        }
    }
    
    private void installBuildHints() {
        checkFaceIDUsageDescription();
    }
    
    public boolean isAvailable() {
        installBuildHints();
        return false;
    }

    public void scan(String reason) {
        installBuildHints();
    }

    public boolean isSupported() {
        installBuildHints();
        return false;
    }
    
    public void addPassword(int requestId, String reason, String key, String value) {
        installBuildHints();
        InternalCallback.requestError(requestId, "addSecureItem not supported on this platform");
        
    }
    public void deletePassword(int requestId, String reason, String key) {
        installBuildHints();
        InternalCallback.requestError(requestId, "deleteSecureItem not supported on this platform");
    }
    public void getPassword(int requestId, String reason, String key) {
        installBuildHints();
        InternalCallback.requestError(requestId, "getSecureItem not supported on this platform");
    }
    
    public void cancelRequest(int requestId) {}

}
