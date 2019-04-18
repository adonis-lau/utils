package bid.adonis.lau.test;

import bid.adonis.lau.entity.Constant;
import bid.adonis.lau.service.JobService;
import chinatelecom.feilong.scheduler.entity.Job;
import chinatelecom.feilong.scheduler.entity.JobConfig;
import chinatelecom.feilong.scheduler.entity.JobParams;
import chinatelecom.feilong.scheduler.entity.plugins.BasePlugin;
import chinatelecom.feilong.scheduler.entity.plugins.Jar;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.enumeration.SchedulerType;
import chinatelecom.feilong.scheduler.service.SchedulerService;
import chinatelecom.feilong.scheduler.utils.JSONObject;
import chinatelecom.feilong.scheduler.utils.JobPluginUtils;
import chinatelecom.feilong.scheduler.utils.JobUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Adonis Lau
 * @date: 2019/1/25 10:38
 */
public class Test {
    public static void main(String[] args) {
        SchedulerService.init(Constant.IP, Constant.PORT, Constant.CONTEXT);
        JobService jobService = new JobService();
        Test test = new Test();
        Job job = test.createJob("", "");
//        Job job = test.createJob(args[0], args[1]);
        GeneralResponse publistResponse = jobService.publishJob(job);
        System.out.println(job);
        System.out.println(JSONObject.toJSONString(publistResponse));
    }

    /**
     * 创建作业
     */
    private Job createJob(String file, String filePath) {

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
            File shellDepFile = FileUtils.getFile(file);
            allDepFile.add(shellDepFile);
            BasePlugin<Jar> jar = JobPluginUtils.getJar("jar_test", filePath, "chinatelecom.feilong.meepo.webservice.WebService", "123 456 789", (String) null, null);


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
                    Constant.TASK_NAME, Constant.JOB_DESCRIPTION, jobParams, jobConfig, allDepFile, jar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将创建完成的作业返回
        return job;
    }
}
