package cc.phos.utils;

public class IDUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";
    private static final int LENGTH = 8;

    public static String generateUserID() {
        return StringUtil.genRandomString(CHARACTERS, LENGTH);
    }
}
