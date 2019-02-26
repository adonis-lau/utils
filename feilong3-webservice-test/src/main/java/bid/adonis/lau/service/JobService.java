package bid.adonis.lau.service;

import chinatelecom.feilong.scheduler.entity.Job;
import chinatelecom.feilong.scheduler.entity.JobConfig;
import chinatelecom.feilong.scheduler.entity.exception.SchedulerException;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.service.SchedulerService;

import java.io.IOException;

/**
 * 作业创建、发布、运行、调度开启关闭等
 *
 * @author: Adonis Lau
 * @date: 2018/11/14 11:20
 */
public class JobService {

    /**
     * 发布作业
     */
    public GeneralResponse publishJob(Job job) {
        GeneralResponse generalResponse = null;
        try {
            // 调用作业发布方法，传入作业文件流发布作业
            generalResponse = SchedulerService.publishJob(job);
        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
        return generalResponse;
    }

    /**
     * 设置调度策略
     */
    public GeneralResponse setSchedulerConfig(String jobName, String projectId, JobConfig jobConfig) {
        GeneralResponse generalResponse = null;
        try {
            // 调用设置调度策略方法，修改作业的调度策略
            generalResponse = SchedulerService.setJobPolicy(jobName, projectId, jobConfig);
        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
        return generalResponse;
    }

    /**
     * 删除作业
     */
    public GeneralResponse deleteJob(String jobName, String projectId) {
        GeneralResponse generalResponse = null;
        try {
            // 调用作业删除方法删除作业
            generalResponse = SchedulerService.jobDelete(jobName, projectId);
        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
        return generalResponse;
    }

}
