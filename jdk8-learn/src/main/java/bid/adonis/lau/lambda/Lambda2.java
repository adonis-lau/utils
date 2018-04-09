package bid.adonis.lau.lambda;

import bid.adonis.lau.lambda.entity.Person;

import java.util.Arrays;
import java.util.List;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/3/23 14:22
 */
public class Lambda2 {
    //打印出guiltyPersons List里面所有LastName以"Z"开头的人的FirstName
    public static void main(String[] args) {
        List<Person> guiltyPersons = Arrays.asList(
                new Person("Yanhua", "Hu", 18),
                new Person("Deshuai", "Liu", 18),
                new Person("Qiyue", "Zhang", 18)
        );
    }
}
