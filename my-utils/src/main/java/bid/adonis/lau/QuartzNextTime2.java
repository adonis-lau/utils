package bid.adonis.lau;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: Adonis Lau
 * @email: adonis.lau.dev@gmail.com
 * @date: 2018/3/26 16:17
 */
public class QuartzNextTime2 {
    public static void main(String args[]) throws ParseException {

        String dateExp = "YYYY-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateExp);

//        String exp = "20 45 * ? * MON-FRI";
        String exp = "0 50 18 ? * 2,4,6";
        CronExpression cronExp1 = new CronExpression(exp);

        Calendar instance = Calendar.getInstance();
        Calendar result = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        result.set(Calendar.MILLISECOND, 0);
        current.add(Calendar.MILLISECOND, 1);


        System.out.println(sdf.format(instance.getTime()));
        System.out.println(sdf.format(cronExp1.getNextValidTimeAfter(instance.getTime())));
        System.out.println(sdf.format(result.getTime()));
        System.out.println(sdf.format(cronExp1.getNextValidTimeAfter(result.getTime())));
        System.out.println(sdf.format(current.getTime()));
        System.out.println(sdf.format(cronExp1.getNextValidTimeAfter(current.getTime())));

    }

}
