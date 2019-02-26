package bid.adonis.lau;

import chinatelecom.feilong.scheduler.entity.exception.SchedulerException;
import org.junit.Test;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Adonis Lau
 * @date: 2019/1/7 16:21
 */
public class Other {
    private void checkLabel(String sequence) throws SchedulerException {
        final String format = "[0-9A-Za-z_]*";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        if (!matcher.matches()) {
            throw new SchedulerException("组件名称中只能由数字、字母和下划线组成。");
        }
    }

    @Test
    public void checkTest() {
        String string = "65^%$4 54 56";
        try {
            checkLabel(string);
        } catch (SchedulerException e) {
            System.err.println(e.getMessage());
        }
    }
}
