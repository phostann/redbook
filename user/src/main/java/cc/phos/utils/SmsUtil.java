package cc.phos.utils;

public class SmsUtil {

    private static final String CHARACTERS = "123456789";
    private static final int LENGTH = 6;

    public static String generateSmsCode() {
       return StringUtil.genRandomString(CHARACTERS, LENGTH);
    }
}
