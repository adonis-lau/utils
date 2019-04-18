package bid.adonis.lau;

import bid.adonis.lau.utils.CollectionUtils;
import bid.adonis.lau.utils.DateTimeUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.Map;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/3/1 10:05
 */
public class AdonisTest {

    @Test
    public void stringTest() {
        System.out.println("\\s+");
    }

    @Test
    public void dateTimeTest() {
        String startTime = "2018-03-08 14:01:06";
        String finishTime = "2019-03-08 14:01:17";
        System.out.println(DateTimeUtils.timeDiff(startTime, finishTime, DateTimeUtils.DateTimeFormater.toPattern()));
        String formatDHMS = DateTimeUtils.formatDHMS(DateTimeUtils.timeDiff(startTime, finishTime, DateTimeUtils.DateTimeFormater.toPattern()));
        System.out.println(formatDHMS);

        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(currentTimeMillis);
        System.out.println(new Date().getTime());
    }

    @Test
    public void collection() {
        Integer[] ints = new Integer[10];
        //循环给数组中的每个元素赋初值
        for (int i = 0; i < ints.length; i++) {
            //产生随机数并赋值给数组
            ints[i] = (int) (Math.random() * 100);
        }

        String join = CollectionUtils.join(ints);
        System.out.println(join);
    }

}
