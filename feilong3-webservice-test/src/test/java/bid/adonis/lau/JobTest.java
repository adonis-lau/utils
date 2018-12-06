package bid.adonis.lau;

import bid.adonis.lau.entity.Constant;
import bid.adonis.lau.service.JobService;
import bid.adonis.lau.service.ProcessService;
import chinatelecom.feilong.scheduler.entity.Job;
import chinatelecom.feilong.scheduler.entity.JobConfig;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.service.SchedulerService;
import chinatelecom.feilong.scheduler.utils.DateUtils;
import chinatelecom.feilong.scheduler.utils.JSONObject;
import chinatelecom.feilong.scheduler.utils.JobUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;

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

    @Test
    public void jobDeploy() {
        Job job = jobService.createJob();
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
        GeneralResponse response = processService.jobReExecute(Constant.JOBNAME, Constant.PROJECT_ID, EXECUTION_ID, "shell2");
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
