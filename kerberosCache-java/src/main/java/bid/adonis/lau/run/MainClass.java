package bid.adonis.lau.run;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.security.PrivilegedExceptionAction;


@Slf4j
public class MainClass {

    private static final String CORE_SITE_LOCATION = "/etc/hadoop/conf/core-site.xml";
    private static final String HDFS_SITE_LOCATION = "/etc/hadoop/conf/hdfs-site.xml";
    private static Configuration conf = null;
    private static UserGroupInformation ugi = null;

    public static void main(String[] args) {
        log.info("--------------------start--------------------");
        System.out.println("--------------------start--------------------");
        MainClass mainClass = new MainClass(args[0], args[1]);
        try {
            ugi.doAs((PrivilegedExceptionAction<Void>) () -> {
                System.out.println(args[2] + " exist ? " + mainClass.exits(args[2]));
                return null;
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("--------------------end--------------------");
        System.out.println("--------------------end--------------------");

    }

    public MainClass(String ticketCache, String user) {
        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
        conf = new Configuration();
        conf.addResource(new Path(CORE_SITE_LOCATION));
        conf.addResource(new Path(HDFS_SITE_LOCATION));
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        conf.setBoolean("hadoop.security.authentication", true);
        conf.set("hadoop.security.authentication", "kerberos");
//        conf.set("dfs.namenode.kerberos.principal", "hdfs/_HOST@EXAMPLE.COM");  //hdfs-site.xml中配置信息
//        conf.set("dfs.datanode.kerberos.principal", "hdfs/ddp-jsdn@EXAMPLE.COM");  //hdfs-site.xml中配置信息
        UserGroupInformation.setConfiguration(conf);
        try {
            //kerberos 认证
            ticketCache = FileUtils.getFile(ticketCache).getAbsolutePath();
            System.out.println(ticketCache);
            System.out.println(user);
            System.out.println("-----------------------------------");
            ugi = UserGroupInformation.getUGIFromTicketCache(ticketCache, user);
            System.out.println("currentUserName is " + ugi.getUserName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param path
     * @return
     * @throws Exception
     */
    public boolean exits(String path) throws Exception {

        FileSystem fs = FileSystem.get(conf);
        return fs.exists(new Path(path));
    }

}
