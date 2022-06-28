package com.espritx.client.services.User;

import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkManager;
import com.espritx.client.entities.User;
import com.espritx.client.utils.FileUtils;
import com.espritx.client.utils.MimeTypeMap;
import com.espritx.client.utils.Statics;

import java.io.IOException;


public class ProfileService {
    public static User edit_profile(User newUserData, String avatarPath) throws IOException {
        MimeTypeMap mimeTypeMap = new MimeTypeMap();
        MultipartRequest updateProfileRequest = new MultipartRequest();
        updateProfileRequest.addData("avatarFile", avatarPath, mimeTypeMap.getMimeType(FileUtils.getFileExtension(avatarPath)));
        updateProfileRequest.addArgumentNoEncoding("userProfile", newUserData.getPropertyIndex().toJSON());
        updateProfileRequest.setPost(true);
        updateProfileRequest.setUrl(Statics.BASE_URL + "/profile/edit/" + AuthenticationService.getAuthenticatedUser().id.get());
        NetworkManager.getInstance().addToQueueAndWait(updateProfileRequest);
        return UserService.fetchUserByEmail(newUserData.email.get());
    }
}
