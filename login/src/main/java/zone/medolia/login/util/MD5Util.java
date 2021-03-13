package zone.medolia.login.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String SERVER_SALT = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String str = "" + SERVER_SALT.charAt(0) + SERVER_SALT.charAt(2)
                + inputPass + SERVER_SALT.charAt(5) + SERVER_SALT.charAt(4);

        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2)
                + formPass + salt.charAt(5) + salt.charAt(4);

        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    /*public static void main(String[] args) {
        System.out.println(MD5Util.inputPassToDBPass("123456", "1a2b3c"));
    }*/
}
