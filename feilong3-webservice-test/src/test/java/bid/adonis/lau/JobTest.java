package bid.adonis.lau;

import bid.adonis.lau.entity.Constant;
import bid.adonis.lau.service.JobService;
import bid.adonis.lau.service.ProcessService;
import chinatelecom.feilong.scheduler.entity.Job;
import chinatelecom.feilong.scheduler.entity.JobConfig;
import chinatelecom.feilong.scheduler.entity.JobParams;
import chinatelecom.feilong.scheduler.entity.plugins.*;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.enumeration.LineColor;
import chinatelecom.feilong.scheduler.enumeration.SSHTimeout;
import chinatelecom.feilong.scheduler.enumeration.SchedulerType;
import chinatelecom.feilong.scheduler.service.SchedulerService;
import chinatelecom.feilong.scheduler.utils.DateUtils;
import chinatelecom.feilong.scheduler.utils.JSONObject;
import chinatelecom.feilong.scheduler.utils.JobPluginUtils;
import chinatelecom.feilong.scheduler.utils.JobUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Adonis Lau
 * @date: 2018/11/15 10:41
 */
public class JobTest {

    private JobService jobService = new JobService();
    private ProcessService processService = new ProcessService();

    private static final String EXECUTION_ID = "flow_1543483876313_jfxxlzzw";

    @Before
    public void init() {
        // 初始化作业发布服务
//        SchedulerService.init("work", 8080, "feilong3");
        SchedulerService.init(Constant.IP, Constant.PORT, Constant.CONTEXT);
    }

    /**
     * 创建作业
     */
    private Job createJob() {

        Job job = null;
        try {
            // allDepFile用于保存所有组件中引用到的本地文件对象
            List<File> allDepFile = new ArrayList<>();

            /*创建组件*/
            // 创建Shell组件
            // 引用“用户空间文件”下的 test/001/abc.txt 这个文件。
            String shellDepFilePath = FileUtils.getFile("test/001/abc.txt").getPath();
            BasePlugin<Shell> shell = JobPluginUtils.getShell("shell_test", "cat ./test/001/abc.txt", null, shellDepFilePath, null);

            // 创建Jar组件
            // 引用本地的 jar-test2.jar 文件，并添加到 allDepFile 中
            File jarPluginDepJar = FileUtils.getFile("/data/tmp/jar-test2.jar");
            allDepFile.add(jarPluginDepJar);
            // 组件中可以只传入依赖文件的部分路径，该路径即为程序中指定的相对路径
            String jarPluginDepJarPath = FileUtils.getFile("tmp/jar-test2.jar").getPath();
            BasePlugin<Jar> jar = JobPluginUtils.getJar("jar_test", jarPluginDepJarPath, "chinatelecom.feilong.meepo.webservice.WebService", "111 222 333 444", (String) null, null);
            // 创建Python组件
            BasePlugin<Python> python = JobPluginUtils.getPython("python_test", "print('123')", null, (String) null);
            // 创建SSH组件
            BasePlugin<SSH> ssh = JobPluginUtils.getSSH("ssh_test", Constant.SSH_IP, Constant.SSH_PORT, Constant.SSH_USERNAME, Constant.SSH_PASSWORD, "java -version", SSHTimeout.OneHour);

            /*设置组件依赖关系*/
            // shell/ssh 不依赖其他节点，默认设置继承于开始节点，并列第一运行
            // shell运行出错才能运行jar
            jar.setDependencies(shell, LineColor.red);
            // shell运行出错且jar运行成功才能运行python
            python.setDependencies(shell, LineColor.red);
            python.setDependencies(jar);

            /*设置作业参数*/
            JobParams jobParams = JobUtils.getJobParams("pathValue", "var date = new Date();\n" +
                    "var year = date.getFullYear();\n" +
                    "var month = date.getMonth() + 1;\n" +
                    "if(month < 10){\n" +
                    "  month = \"0\" + month;\n" +
                    "}\n" +
                    "var day = date.getDate();\n" +
                    "var hour = date.getHours();\n" +
                    "var minutes = date.getMinutes();\n" +
                    "if(minutes < 10){\n" +
                    "  minutes = \"0\" + minutes;\n" +
                    "}\n" +
                    "var pathValue = \"\" + year + month + day + hour + minutes;");

            /*设置作业调度策略*/
            JobConfig jobConfig = JobUtils.getJobConfig(SchedulerType.WEEKLY, 0, 0, 0, 1, 0);
            /*组合作业*/
            job = JobUtils.getJob(Constant.JOBNAME, Constant.PROJECT_ID, Constant.USERNAME, Constant.SYSTEM_NAME,
                    Constant.TASK_NAME, Constant.JOB_DESCRIPTION, jobParams, jobConfig, allDepFile, shell, jar, python, ssh);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将创建完成的作业返回
        return job;
    }


    @Test
    public void jobDeploy() {
        Job job = createJob();
        GeneralResponse publistResponse = jobService.publishJob(job);
        System.out.println(job);
        System.out.println(JSONObject.toJSONString(publistResponse));
    }

    @Test
    public void setSchedulerConfig() {
        JobConfig jobConfig = JobUtils.getJobConfig("0 1,11,21,31,41,51 * * * ? ");
        GeneralResponse response = jobService.setSchedulerConfig(Constant.JOBNAME, Constant.PROJECT_ID, jobConfig);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void openScheduler() {
        GeneralResponse response = processService.openScheduler(Constant.JOBNAME, Constant.PROJECT_ID);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void closeScheduler() {
        GeneralResponse response = processService.closeScheduler(Constant.JOBNAME, Constant.PROJECT_ID);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void runOnce() {
        GeneralResponse response = processService.runOnce(Constant.JOBNAME, Constant.PROJECT_ID);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void jobSuspend() {
        GeneralResponse response = processService.jobSuspend(Constant.JOBNAME, Constant.PROJECT_ID, EXECUTION_ID);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void jobContinue() {
        GeneralResponse response = processService.jobContinue(Constant.JOBNAME, Constant.PROJECT_ID, EXECUTION_ID);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void setRuntime() {
        Date runTime = DateUtils.addMinutes(new Date(), 2);
        GeneralResponse response = processService.setRuntime(Constant.JOBNAME, Constant.PROJECT_ID, runTime);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void jobKill() {
        GeneralResponse response = processService.jobKill(Constant.JOBNAME, Constant.PROJECT_ID, EXECUTION_ID);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void jobReExecute() {
        GeneralResponse response = processService.jobReExecute(Constant.JOBNAME, Constant.PROJECT_ID, EXECUTION_ID, "python_test");
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void jobCheck() throws InterruptedException {
        for (int i = 0; i < 200; i++) {
            GeneralResponse response = processService.jobCheck(Constant.JOBNAME, Constant.PROJECT_ID, EXECUTION_ID);
            System.out.println(JSONObject.toJSONString(response));
            Thread.sleep(1000 * 2);
        }
    }

    @Test
    public void downloadLog() throws IOException {
        InputStream inputStream = processService.downloadLog(Constant.JOBNAME, Constant.PROJECT_ID, EXECUTION_ID);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            System.out.println(s);
        }
    }

    @Test
    public void jobDelete() {
        GeneralResponse response = jobService.deleteJob(Constant.JOBNAME, Constant.PROJECT_ID);
        System.out.println(JSONObject.toJSONString(response));
    }
}
