package bid.adonis.lau;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/3/1 10:05
 */
public class AdonisTest {

    @Test
    public void fileTest() throws IOException {
//        File source = new File("C:\\Users\\adonis\\Desktop\\tmp.txt");
        File source = new File("C:\\Users\\adonis\\Desktop\\aaa\\crypt_test_source_file_001.tar.gz");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(source))));
        String str = null;
        int i = 0;
        while ((str = bufferedReader.readLine()) != null) {
            System.out.println("line num = " + ++i);
//            System.out.println((str.contains("\n") || str.contains("\r\n") || str.contains("\r")) ? "empty or null" : "");
            if (i == 1001) {
                char c = str.charAt(0);
                char b = str.charAt(str.length() - 1);
                System.out.println(c);
                System.out.println(b);
                System.out.println((StringUtils.isBlank(str) || (str.charAt(0) == '\u0000' && str.charAt(str.length() - 1) == '\u0000')) ? "empty or null" : str );
            }
            //            tmp(str);
        }
    }

    private void tmp(String str) {
        String[] split = str.split("\t");
        System.out.println("split.length = " + split.length);
            System.out.println(StringUtils.isBlank(StringUtils.chomp(str)) ? "empty or null" : "");
    }

}
