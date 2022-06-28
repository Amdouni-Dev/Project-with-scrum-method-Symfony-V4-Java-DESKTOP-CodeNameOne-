package com.espritx.client.utils;

public class FileUtils {
    public static String getFileExtension(String path) {
        String[] parts = StringUtils.split(path, ".");
        return parts[parts.length - 1];
    }
}

