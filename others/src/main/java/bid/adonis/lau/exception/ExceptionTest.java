package bid.adonis.lau.exception;

import java.util.List;

/**
 * @author: Adonis Lau
 * @date: 2018/12/28 13:22
 */
public class ExceptionTest {
    public static void main(String[] args) {
        Exception2 exception2 = new Exception2();
        try {
            exception2.exception();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
