package chinatelecom.feilong.meepo.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2017/11/27 14:25
 */
public class Email {

    public static final String AUTOGRAPH = "<p>\n" +
            "\t&nbsp;</p>\n" +
            "<hr align=\"left\" color=\"#b5c4df\" size=\"1\" style=\"box-sizing: border-box; color: rgb(0, 0, 0); font-family: &quot;Microsoft YaHei UI&quot;; font-size: 14px; line-height: 21px; width: 210px; height: 1px;\" />\n" +
            "<div style=\"color: rgb(0, 0, 0); font-family: &quot;Microsoft YaHei UI&quot;; font-size: 14px; line-height: 21px;\">\n" +
            "\t<div style=\"position: static !important; margin: 10px; font-family: verdana; font-size: 10pt;\">\n" +
            "\t\t<div style=\"font-family: Arial; font-size: 14px; line-height: 23px;\">\n" +
            "\t\t\t<div>\n" +
            "\t\t\t\t<b>刘德帅</b></div>\n" +
            "\t\t\t<div>\n" +
            "\t\t\t\t<b>大数据业务部</b></div>\n" +
            "\t\t</div>\n" +
            "\t\t<div style=\"font-family: Arial; font-size: 14px; line-height: 23px;\">\n" +
            "\t\t\t<span style=\"background-color: rgba(0, 0, 0, 0);\">中国电信-上海理想信息产业（集团）有限公司</span></div>\n" +
            "\t\t<div style=\"font-family: Arial; font-size: 14px; line-height: 23px;\">\n" +
            "\t\t\t手机：17637275713</div>\n" +
            "\t\t<div style=\"font-family: Arial; font-size: 14px; line-height: 23px;\">\n" +
            "\t\t\t邮箱：<a href=\"mailto:13651752314@189.cn\" style=\"text-decoration: none !important; font-family: verdana; font-size: 10pt; line-height: 1.5; background-color: window;\">17637275713</a><a class=\"fox_hand\" href=\"mailto:13651752314@189.cn\" style=\"text-decoration: none !important; font-family: verdana; font-size: 10pt; line-height: 1.5; background-color: window;\">@189.cn</a></div>\n" +
            "\t\t<div style=\"font-family: Arial; font-size: 14px; line-height: 23px;\">\n" +
            "\t\t\t地址：上海市浦东新区丁香路750号</div>\n" +
            "\t\t<div style=\"font-family: Arial; font-size: 14px; line-height: 23px;\">\n" +
            "\t\t\t<b style=\"color: rgb(0, 0, 255);\">数论智慧，践行卓越！</b></div>\n" +
            "\t</div>\n" +
            "</div>\n" +
            "<p>\n" +
            "\t&nbsp;</p>\n";

    public static void main(String[] args) {
        String nick = "admin";
        String from = "adonis@liudeshuai.cn";
        String password = "Liu546085758";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.exmail.qq.com");
        props.put("mail.smtp.port", 25);

        Session session = Session.getInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(nick + "<" + from + ">");
            msg.setRecipients(Message.RecipientType.TO, "17637275713@189.cn");
            msg.setSubject("测试邮件");
            msg.setSentDate(new Date());
            msg.setContent("这是一封测试邮件。来自我的企业邮箱。" + AUTOGRAPH, "text/html;charset=utf-8");
            Transport.send(msg, from, password);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
