package bid.adonis.lau;

import bid.adonis.lau.service.JobService;
import chinatelecom.feilong.scheduler.entity.Job;
import chinatelecom.feilong.scheduler.entity.JobConfig;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.utils.JSONObject;
import chinatelecom.feilong.scheduler.utils.JobUtils;
import org.junit.Test;

/**
 * @author: Adonis Lau
 * @date: 2018/11/15 10:41
 */
public class JobTest {
    private JobService jobService = new JobService();

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

}
