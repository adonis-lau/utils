package chinatelecom.feilong.meepo.msg;

import com.caucho.hessian.client.HessianProxyFactory;
import com.cdc.smgp.msg.Result;
import com.cdc.smgp.msg.service.IMsgService;

import java.net.MalformedURLException;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2017/11/26 11:00
 */
public class Sms {

    //    private static final String MSG_URL = "http://10.34.1.135:8193/IdopSms/remoting/msgService";
    private static final String MSG_URL = "http://111.235.158.129:8193/IdopSms/remoting/msgService";

    public static void main(String[] args) {

        HessianProxyFactory factory = new HessianProxyFactory();

        try {
            IMsgService msgService = (IMsgService) factory.create(IMsgService.class, MSG_URL);
            String[] mobile = {"17637275713", "17703725713"};
            Result result = msgService.sendMsg("MonitorSys", "608099c83e18fbd03dfb4733aa5f1747", mobile, "这是一条测试短信。");
            if (result.getResult() == 1) {
                System.out.println(result.getResult());
                System.out.println(result.getDescription());
            } else if (result.getResult() == -1) {
                System.out.println(result.getResult());
                System.out.println(result.getDescription());
            } else {
                System.out.println(result.getResult());
                System.out.println(result.getDescription());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
