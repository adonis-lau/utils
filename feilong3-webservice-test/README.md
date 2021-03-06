#飞龙3任务调度接口调用Jar包使用样例

## 将Jar包安装到本地Maven仓库
mvn install:install-file -DgroupId=chinatelecom.feilong3 -DartifactId=scheduler-interface -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dfile=D:\Workspaces\IdeaProjects\Work\feilong\feilong3.0\util-common\scheduler-interface\deploy\scheduler-interface-1.0-SNAPSHOT.jar
<br/>
mvn install:install-file -DgroupId=chinatelecom.feilong3 -DartifactId=scheduler-interface -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dfile=D:\Workspaces\IdeaProjects\Work\feilong\feilong3.0\util-common\scheduler-interface\target\scheduler-interface-1.0-SNAPSHOT-sources.jar -Dclassifier=sources
<br/>

## Jar包在pom中的引用
```
<dependency>
    <groupId>chinatelecom.feilong</groupId>
    <artifactId>scheduler-interface</artifactId>
    <version>1.0</version>
</dependency>
```

##接口列表：
* 发布作业
* 设置调度策略
* 删除作业
* 开启调度
* 关闭调度
* 运行一次
* 事件触发
* 作业暂停
* 继续运行
* 作业强杀
* 作业重跑
* 查看执行结果
* 获取运行日志

##Jar包使用方法
* ###所有接口调用前，都应该调用 SchedulerService.init(String ip, int port, String context); 方法，初始化接口信息。
  * 参数说明：
    * ip：任务调度web服务的ip地址
    * port：任务调度web服务的端口
    * context：任务调度web服务的名称

* ###发布作业（发布作业分两个步骤：创建作业，调用作业发布方法）
  * ####创建作业
    * 组件，是作业的最小组成结构。运行作业，就是按照用户自定义的顺序，依次运行组件。使用 JobPluginUtils 类中的get方法,传入正确的参数，便可以获取到对应的功能组件。各个组件的组件名称（label，所有get方法中的第一个参数）不可以相同。例:<br/>
      ###### public static BasePlugin<Shell> getShell(String label, String scriptcontent, String command_args, String file, String output_args)
      * label: 组件名称（节点名称）
      * scriptcontent: Shell脚本内容
      * command_args: 命令行参数，即要传入Shell脚本的参数列表
      * file: 执行Shell脚本时用到的依赖文件
      * output_args: Shell参数执行完，想要传递给下一个组件的参数
    * 创建组件时引入依赖文件（例：Jar组件的依赖文件，就是一个或者多个Jar包等），有两种方式（两种方式可以兼容。即 List 中可以一部分是本地文件路径，另一部分是空间文件的路径）。
      1. 引用本地文件。在使用JobPluginUtils获取组件对象时，传入本地的`文件路径`或者`文件路径List`。（这里使用的，只是文件路径。文件本身，需要在使用JobUtils获取Job对象时，传入List\<File>）
      2. 引用“用户空间管理”中的文件。在使用JobPluginUtils获取组件对象时，传入`用户空间`中的`文件路径`或者`文件路径List`。
    * `BasePlugin<Shell> shell = JobPluginUtils.getShell("shell_test", "cat ./test/001/abc.txt", null, "test/001/abc.txt", null);`  便是创建了一个名称叫做“shell_test”、脚本内容是“cat ./test/001/abc.txt”、依赖文件为"test/001/abc.txt"、没有输入参数和输出参数的shell组件。在作业运行到这个组件的时候，会执行shell脚本中的内容，打印出abc.txt文件中的内容。
    * 设置组件之间的依赖关系。
      * 组件之间，依赖关系有三种：
        1. LineColor.green（绿色连线。父节点运行成功，才运行当前组件）
        2. LineColor.red（红色连线。父节点运行失败，才运行当前组件）
        3. LineColor.black（黑色连线。父节点运行成功或失败，都运行当前组件）
      * 使用组件的setDependencies方法设置依赖关系。
        1. 一个组件，可以有多个父级依赖。组件与组件之间，应该构成`有向无环`图。
        2. 可以只设置依赖组件，而不设置连线颜色。连线颜色默认值为LineColor.green。
      * 依赖关系设置示例：
        1. `python.setDependencies(jar);` 表示jar组件运行成功了，才会运行jar组件。（没有指明LineColor，默认为green）
        2. `jar.setDependencies(shell, LineColor.red);` 表示shell组件运行失败了，才会运行jar组件。
        3. `mr.setDependencies(shell, LineColor.black);` 表示shell组件运行完成后，运行jar组件。（无论shell组件是成功还是失败）
    * 设置作业参数。JobUtils.getJobParams(String key, String value); 方法，有两个参数。
      1. 第一个参数为 key ，多个key之间用 `,` 分割。例：pathValue,minutes
      2. 第二个参数为 value，格式应该符合JS规范。例：
      ```javascript
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        if(month < 10){
          month = "0" + month;
        }
        var day = date.getDate();
        var hour = date.getHours();
        var minutes = date.getMinutes();
        if(minutes < 10){
          minutes = "0" + minutes;
        }
        var pathValue = "" + year + month + day + hour + minutes;
      ```
    * 设置作业运行监控通知
      * 使用 JobUtils.getEmail() 或 JobUtils.getSms() 获取JobResultNotification对象
      * 例如
        ```
          JobResultNotification email = JobUtils.getEmail("adonis", "jobResultNotification", "12345678@345.com", NotificationSendMoment.FailSend);
        ```
    * 设置调度策略
      * 使用 JobUtils.getJobConfig() 方法可以获取调度策略对象。
      * 调度策略可以按照 `秒，分，时，周，月` 进行设置，也可以使用`crontab`表达式进行设置。
      * 调度时间间隔必须大于10分钟。
      * 例如:
        ```
          JobConfig jobConfig = JobUtils.getJobConfig(SchedulerType.WEEKLY, 0, 0, 0, 1, 0);
        ```
        表示调度策略设置为每周第一天0点0分0秒
    * 创建作业
      * 使用 JobUtils.getJob() 方法，传入对应参数（作业名、项目ID、用户名、运行参数、调度策略、依赖文件、组件等），获取作业对象。
      * 例如：
      ```
        方法：
        public static Job getJob(String jobName, String projectId, String username, String systemName, String taskName, String jobDescription, JobParams jobParams, JobConfig jobConfig, List<File> dependentFiles, BasePlugin... plugins)
        方法的使用：
        Job job = JobUtils.getJob("job_publish_test_1543476609886", "40288df2631f723801632546c8f60321", "meepo_job", "meepo", "test", "测试作业", jobParams, jobConfig, dependentFiles, shell, jar, python, mr);
        
        方法（带邮件通知）：
        public static Job getJob(String jobName, String projectId, String username, String systemName, String taskName, String jobDescription, JobParams jobParams, JobResultNotification jobResultNotification, JobConfig jobConfig, List<File> dependentFiles, BasePlugin... plugins)
        方法的使用：
        Job job = JobUtils.getJob("job_publish_test_1543476609886", "40288df2631f723801632546c8f60321", "meepo_job", "meepo", "test", "测试作业", jobParams, email, jobConfig, dependentFiles, shell, jar, python, mr);

      ```
      这样我们便创建了一个作业名为 job_publish_test_1543476609886 、归属于项目 40288df2631f723801632546c8f60321 和 用户 meepo_job 的作业。该作业包含了作业运行参数 jobParams 、调度策略为 jobConfig 、依赖文件为 dependentFiles 。包含了 shell、 jar、 python和mr等四个组件。
  * ####调用作业发布方法
    * 调用 SchedulerService.publishJob(Job job) ，传入之前创建的作业即可发布作业。
    
* ###设置调度策略（不修改作业内容，只修改作业的调度策略）
  * 使用 JobUtils.getJobConfig() 方法可以获取调度策略对象。
  * 调度策略可以按照 `秒，分，时，周，月` 进行设置，也可以使用`crontab`表达式进行设置。
  * 调度时间间隔必须大于10分钟。
  * 调用 SchedulerService.setJobPolicy(String jobName, String projectId, JobConfig jobConfig) 方法设置作业的调度策略。
  
* ###删除作业
  * 调用 SchedulerService.jobDelete(String jobName, String projectId) 方法即可删除指定作业。
  
* ###开启调度
  * 调用 SchedulerService.jobScheduleOpen(String jobName, String projectId) 方法即可开启指定作业的调度。
  
* ###关闭调度
  * 调用 SchedulerService.jobScheduleClose(String jobName, String projectId) 方法即可关闭指定作业的调度。
  
* ###运行一次
  * 调用 SchedulerService.jobExecute(String jobName, String projectId) 方法即可运行指定作业一次。
  * 调用 SchedulerService.jobExecute(String jobName, String projectId, JobParams jobParams) 方法即可运行指定作业一次，且运行时，可以指定作业参数。
    * 作业参数的设置请参考 `发布作业` 方法说明中的 `设置作业参数` 一栏。
  * 作业运行接口返回值中，有 `executionId` 属性。该属性值将会使用在后续的各个接口中，请务必保留。
  
* ###事件触发
  * 调用 SchedulerService.setRuntime(String jobName, String projectId, Date runtime) 方法即可在指定时间运行指定作业一次。
  * 调用 SchedulerService.setRuntime(String jobName, String projectId, Date runtime, JobParams jobParams) 方法即可在指定时间运行指定作业一次，且运行时，可以指定作业参数。
    * 作业参数的设置请参考 `发布作业` 方法说明中的 `设置作业参数` 一栏。
  * 作业运行接口返回值中，有 `executionId` 属性。该属性值将会使用在后续的各个接口中，请务必保留。
  
* ###作业暂停
  * 调用 SchedulerService.jobSuspend(String jobName, String projectId, String executionId) 方法即可使运行中的作业暂停(中止)。
  * executionId 于 `运行一次` 和 `事件触发` 方法中返回。
  
* ###继续运行
  * 调用 SchedulerService.jobContinue(String jobName, String projectId, String executionId) 方法即可使暂停(中止)的作业继续运行。
  * executionId 于 `运行一次` 和 `事件触发` 方法中返回。

* ###作业强杀
  * 调用 SchedulerService.jobKill(String jobName, String projectId, String executionId) 方法即可杀死正在运行中的作业。
  * executionId 于 `运行一次` 和 `事件触发` 方法中返回。

* ###作业重跑
  * 调用 SchedulerService.jobReExecute(String jobName, String projectId, String executionId, String pluginName) 方法即可从指定组件开始重新运行已经失败的作业。
  * executionId 于 `运行一次` 和 `事件触发` 方法中返回。
  * pluginName 是组件名（节点名称），即 label 。

* ###查看执行结果
  * 调用 SchedulerService.jobCheck(String jobName, String projectId, String executionId) 方法即可查看作业的指定次运行情况。
  * executionId 于 `运行一次` 和 `事件触发` 方法中返回。

* ###获取运行日志
  * 调用 SchedulerService.downloadLog(String jobName, String projectId, String executionId) 方法即可查看作业的指定次运行情况。
  * executionId 于 `运行一次` 和 `事件触发` 方法中返回。
  * 该方法的返回值为InputStream。可使用 `BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));` 转换为BufferedReader进行读取。
