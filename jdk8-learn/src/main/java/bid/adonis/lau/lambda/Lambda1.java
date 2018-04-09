package bid.adonis.lau.lambda;

import bid.adonis.lau.lambda.myInterface.MyLambdaInterface;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/3/23 14:22
 */
public class Lambda1 {

    private static MyLambdaInterface aBlockOfCode = System.out::println;

    private static void encat(MyLambdaInterface myLambda, String s) {
        myLambda.doSomeShit(s);
    }

    public static void main(String[] args) {
        encat(aBlockOfCode, "Hello World !");
    }
}
