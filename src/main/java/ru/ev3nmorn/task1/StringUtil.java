package ru.ev3nmorn.task1;

public class StringUtil {

    public static String replace(String str, char oldChar, char newChar) {
        if (str == null) {
            return str;
        }

        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == oldChar) {
                chars[i] = newChar;
            }
        }

        return new String(chars);
    }

}
