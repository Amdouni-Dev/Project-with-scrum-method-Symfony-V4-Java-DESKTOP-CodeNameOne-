package com.espritx.client.utils;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringUtils {
    // fine.. I'll do it myself...
    public static String[] split(String str, String delimiter)
    {
        ArrayList<String> splitArray = new ArrayList<>();
        StringTokenizer arr = new StringTokenizer(str, delimiter);//split by commas
        while(arr.hasMoreTokens())
            splitArray.add(arr.nextToken());
        return splitArray.toArray(new String[splitArray.size()]);
    }
}
