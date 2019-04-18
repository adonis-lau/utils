package bid.adonis.lau;

import bid.adonis.lau.entity.Constant;
import bid.adonis.lau.service.JobService;
import bid.adonis.lau.service.ProcessService;
import bid.adonis.lau.service.ProjectService;
import chinatelecom.feilong.scheduler.entity.*;
import chinatelecom.feilong.scheduler.entity.exception.SchedulerException;
import chinatelecom.feilong.scheduler.entity.plugins.*;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.enumeration.LineColor;
import chinatelecom.feilong.scheduler.enumeration.NotificationSendMoment;
import chinatelecom.feilong.scheduler.enumeration.SSHTimeout;
import chinatelecom.feilong.scheduler.enumeration.SchedulerType;
import chinatelecom.feilong.scheduler.service.GenerateJobFile;
import chinatelecom.feilong.scheduler.service.SchedulerService;
import chinatelecom.feilong.scheduler.utils.DateUtils;
import chinatelecom.feilong.scheduler.utils.JSONObject;
import chinatelecom.feilong.scheduler.utils.JobPluginUtils;
import chinatelecom.feilong.scheduler.utils.JobUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
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
    private ProjectService projectService = new ProjectService();

    private static final String EXECUTION_ID = "flow_1548385115763_haxkyppg";

    @Before
    public void init() {
        // 初始化作业发布服务
//        SchedulerService.init("work", 8080, "feilong3");
        SchedulerService.initHttp(Constant.IP, Constant.PORT, Constant.CONTEXT);
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
            /*
                shellDepFilePath 可以是 "/test/001/abc.txt" ，也可以是 "test/001/abc.txt" 、 "/001/abc.txt" 、 "001/abc.txt" 、 "/abc.txt" 、 "abc.txt"
                脚本中调用的文件路径引用，要跟这里设置的一致。如 "/test/001/abc.txt" 和 "test/001/abc.txt" 的引用，需要写成 "./test/001/abc.txt"
            */
            String shellDepFilePath = FileUtils.getFile("test/001/abc.txt").getPath();
            BasePlugin<Shell> shell = JobPluginUtils.getShell("shell_test", "cat ./test/001/abc.txt", null, "", null);

            // 创建Python组件
            BasePlugin<Python> python = JobPluginUtils.getPython("python_test", "print('123')", null, (String) null);
            // 创建SSH组件
            BasePlugin<SSH> ssh = JobPluginUtils.getSSH("ssh_test", Constant.SSH_IP, Constant.SSH_PORT, Constant.SSH_USERNAME, Constant.SSH_PASSWORD, "java -version", SSHTimeout.OneHour);

            /*设置组件依赖关系*/
            // shell/ssh 不依赖其他节点，默认设置继承于开始节点，并列第一运行
            // shell运行出错才能运行python
            python.setDependencies(shell, LineColor.red);

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

            JobResultNotification email = JobUtils.getEmail("adonis", "jobResultNotification", "12345678@345.com", NotificationSendMoment.FailSend);
            /*设置作业调度策略*/
            JobConfig jobConfig = JobUtils.getJobConfig(SchedulerType.WEEKLY, 0, 0, 0, 1, 0);
            /*组合作业*/
            job = JobUtils.getJob(Constant.JOBNAME + "_" + System.currentTimeMillis(), Constant.PROJECT_ID, Constant.USERNAME, Constant.PORTAL_USER_ID, Constant.USER_TYPE, Constant.SYSTEM_NAME,
                    Constant.TASK_NAME, Constant.JOB_DESCRIPTION, jobParams, email, jobConfig, allDepFile, shell, python, ssh);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将创建完成的作业返回
        return job;
    }


    /**
     * 创建作业
     */
    private Job createSparkJob() {

        Job job = null;
        try {
            // allDepFile用于保存所有组件中引用到的本地文件对象
            List<File> allDepFile = new ArrayList<>();

            /*创建组件*/
            // 创建Jar组件
            File sparkDepFile = FileUtils.getFile("D:\\Work\\电信理想\\meepo\\飞龙\\测试用例\\spark\\spark-examples_2.11-2.2.0.jar");
            allDepFile.add(sparkDepFile);
            /*
                jarDepFilePath 可以是 "/test/001/abc.txt" ，也可以是 "test/001/abc.txt" 、 "/001/abc.txt" 、 "001/abc.txt" 、 "/abc.txt" 、 "abc.txt"
                脚本中调用的文件路径引用，要跟这里设置的一致。如 "/test/001/abc.txt" 和 "test/001/abc.txt" 的引用，需要写成 "./test/001/abc.txt"
            */
            String sparkDepFilePath = sparkDepFile.getName();
            BasePlugin<Spark> spark = JobPluginUtils.getSpark("spark_test", "org.apache.spark.examples.SparkPi", "count-Pi", "local", sparkDepFilePath, "",
                    "2g", "2", "1", "2", "2", "", "2.2.0", "");

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

            /* 邮件通知 */
            JobResultNotification email = JobUtils.getEmail("adonis", "jobResultNotification", "12345678@345.com", NotificationSendMoment.FailSend);
            /*设置作业调度策略*/
            JobConfig jobConfig = JobUtils.getJobConfig(SchedulerType.WEEKLY, 0, 0, 0, 1, 0);
            /*组合作业*/
            job = JobUtils.getJob(Constant.JOBNAME, Constant.PROJECT_ID, Constant.USERNAME, Constant.PORTAL_USER_ID, Constant.USER_TYPE, Constant.SYSTEM_NAME,
                    Constant.TASK_NAME, Constant.JOB_DESCRIPTION, jobParams, email, jobConfig, allDepFile, spark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将创建完成的作业返回
        return job;
    }


    @Test
    public void genLocalZip() throws IOException {
        Job job = createJob();
        ByteArrayInputStream zipFile = GenerateJobFile.generateZipFile(job);
        OutputStream outputStream = new FileOutputStream("testJob.zip");
        int len = -1;
        byte[] b = new byte[1024];
        while ((len = zipFile.read(b)) != -1) {
            outputStream.write(b, 0, len);
        }
        zipFile.close();
        outputStream.close();

    }

    @Test
    public void jobDeploy() {
        Job job = createJob();
//        Job job = createSparkJob();
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
        for (int i = 0; i < 20; i++) {
            GeneralResponse response = processService.jobCheck(Constant.JOBNAME, Constant.PROJECT_ID, "flow_1550138869530_skbtvfxh");
            System.out.println(JSONObject.toJSONString(response));
            Thread.sleep(1000 * 2);
        }
    }

    @Test
    public void jobDelete() {
        GeneralResponse response = jobService.deleteJob("ML_40288d4868a6d42f0168a81de5d6007b_cycle", "40288dce686560830168656428060002");
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void getProjectInfo() throws SchedulerException {
        GeneralResponse response = projectService.getProjectInfo("1", "", "", "");
//        GeneralResponse response = projectService.getProjectInfo("3", "452", Constant.USERNAME, Constant.PROJECT_ID);
        ProjectInfo projectInfo = new ProjectInfo("1", "", "", "");
        System.out.println(JSONObject.toJSONString(projectInfo));
        System.out.println(JSONObject.toJSONString(response));
    }
}
