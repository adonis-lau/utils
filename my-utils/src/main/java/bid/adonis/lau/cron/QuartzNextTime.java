package bid.adonis.lau.cron;

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
public class QuartzNextTime {
    public static void main(String args[]) throws ParseException {

        String dateExp = "YYYY-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateExp);

        String exp = "20 45 * ? * MON-FRI";
//        String exp = "0 50 18 ? * 2,4,6";
        CronExpression cronExp = new CronExpression(exp);

        Calendar calendar = Calendar.getInstance();

        int i = 0;
        while (i < 0) {
            //获取距指定时间最近的一次触发时间
            Date triggerDate = cronExp.getNextValidTimeAfter(calendar.getTime());
            if (triggerDate == null) {
                System.out.println("没有合适的执行时间节点，流程结束");
            } else {
                System.out.println("下次执行时间节点为: " + sdf.format(triggerDate));
            }
            //让指定时间跳过下次触发时间，以计算出下下次得触发时间
            calendar.setTime(triggerDate);
            calendar.add(Calendar.SECOND, 1);
            i++;
        }


    }

}
