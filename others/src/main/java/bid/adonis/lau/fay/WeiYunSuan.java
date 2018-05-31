package bid.adonis.lau.fay;

import java.util.Base64;

public class WeiYunSuan {
    public static void main(String[] args) {
        String passwd = "123456";
        byte[] passwdBytes = passwd.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        String encodeToString = encoder.encodeToString(passwdBytes);
        System.out.println(encodeToString);
        char[] chars = encodeToString.toCharArray();
        for (char aChar : chars) {
            int i = aChar << 2;
//            char i = (char) ((aChar << 2) + '0');
            System.out.print((char) ((aChar >> 2) + '0'));
        }
    }
}
