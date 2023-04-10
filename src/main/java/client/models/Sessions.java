package client.models;

import java.util.Arrays;

public class Sessions {
    private static final String[] sessionList = {"Automne", "Hiver", "Ete"};
    private static String regexString;

    public static String[] getSessions() {
        return sessionList.clone();
    }

    public static String getRegexString() {
        if (regexString == null) {
            regexString = Arrays.stream(sessionList).reduce((a, b) -> a + "|" + b).get();
        }
        return regexString;
    }
}
