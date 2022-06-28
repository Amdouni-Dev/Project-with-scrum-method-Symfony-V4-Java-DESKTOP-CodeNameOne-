package edu.esprit.utils;

public class StringUtils {
    public static String camelCaseToTitleCase(String camelCaseString) {
        String[] words = camelCaseString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        StringBuilder titleCaseString = new StringBuilder();
        for (String word : words) {
            titleCaseString.append(word.substring(0, 1).toUpperCase());
            titleCaseString.append(word.substring(1).toLowerCase());
            titleCaseString.append(" ");
        }
        return titleCaseString.toString().trim();
    }
}
