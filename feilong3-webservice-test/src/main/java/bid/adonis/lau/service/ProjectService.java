package bid.adonis.lau.service;

import chinatelecom.feilong.scheduler.entity.exception.SchedulerException;
import chinatelecom.feilong.scheduler.entity.response.GeneralResponse;
import chinatelecom.feilong.scheduler.service.SchedulerService;

import java.io.IOException;

/**
 * @author: Adonis Lau
 * @date: 2019/1/2 11:04
 */
public class ProjectService {

    /**
     * 查询项目信息
     *
     * @param roleId    角色ID
     * @param accountId 企业ID
     * @param username  用户名
     * @param projectId 项目ID
     * @return
     */
    public GeneralResponse getProjectInfo(String roleId, String accountId, String username, String projectId) {
        GeneralResponse generalResponse = null;
        try {
            // 开启调度
            generalResponse = SchedulerService.getProjectInfo(roleId, accountId, username, projectId);
        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
        return generalResponse;
    }
}
