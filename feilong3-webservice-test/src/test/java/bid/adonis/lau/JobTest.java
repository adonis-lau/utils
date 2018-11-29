package bid.adonis.lau;

import bid.adonis.lau.entity.Constant;
import bid.adonis.lau.service.JobService;
import chinatelecom.feilong.scheduler.entity.Job;
import chinatelecom.feilong.scheduler.entity.JobConfig;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.service.SchedulerService;
import chinatelecom.feilong.scheduler.utils.JSONObject;
import chinatelecom.feilong.scheduler.utils.JobUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: Adonis Lau
 * @date: 2018/11/15 10:41
 */
public class JobTest {

    private JobService jobService = new JobService();

    @Before
    public void init() {
        // 初始化作业发布服务
//        SchedulerService.init("work", 8080, "feilong3");
        SchedulerService.init(Constant.IP, Constant.PORT, Constant.CONTEXT);
    }

    @Test
    public void jobTest() {
        Job job = jobService.createJob();
        GeneralResponse publistResponse = jobService.publishJob(job);
        System.out.println(job);
        System.out.println(JSONObject.toJSONString(publistResponse));

        JobConfig jobConfig = JobUtils.getJobConfig("1,23,45 * * * * ?");
        GeneralResponse configResponse = jobService.setSchedulerConfig(job.getJobName(), job.getProjectId(), jobConfig);
        System.out.println(JSONObject.toJSONString(configResponse));

    }

    @Test
    public void jobDelete() {
        GeneralResponse response = jobService.deleteJob("webServiceTest_1542553366109", Constant.PROJECT_ID);
        System.out.println(JSONObject.toJSONString(response));
    }
}
