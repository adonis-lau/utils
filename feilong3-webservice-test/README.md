#飞龙3任务调度接口调用Jar包使用样例

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
* ###发布作业（发布作业分两个步骤：创建作业，调用作业发布方法）
  * ####创建作业
    * 使用 JobPluginUtils 类中的get方法可以获取到对应的功能组件。例如getShell、getJar和getMR等。各个组件的组件名称（label）不可以相同。
    * 使用 JobUtils 类中的getJob方法可以获取到作业对象。getJobParams方法可以获取到作业的运行参数对象。getJobConfig方法可以获取到作业调度策略对象。
    * 创建组件时引入依赖文件（例：Jar组件的依赖文件，就是一个或者多个Jar包等），有两种方式（两种方式可以兼容。即 List 中可以一部分是本地文件路径，另一部分是空间文件的路径）。
      1. 引用本地文件。在使用JobPluginUtils获取组件对象时，传入本地的`文件路径`或者`文件路径List`。（这里使用的，只是文件路径。文件本身，需要在使用JobUtils获取Job对象时，传入List\<File>）
      2. 引用“用户空间管理”中的文件。在使用JobPluginUtils获取组件对象时，传入`用户空间`中的`文件路径`或者`文件路径List`。
    * 设置组件之间的依赖关系。
      * 组件之间，依赖关系有三种：
        1. LineColor.green（绿色连线。父节点运行成功，才运行当前组件）
        2. LineColor.red（红色连线。父节点运行失败，才运行当前组件）
        3. LineColor.black（黑色连线。父节点运行成功或失败，都运行当前组件）
      * 使用组件的setDependencies方法设置依赖关系。
        1. 一个组件，可以有多个父级依赖。组件与组件之间，应该构成`有向无环`图。
        2. 可以只设置依赖组件，而不设置连线颜色。连线颜色默认值为LineColor.green。
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
    * 设置调度策略
      * 使用 JobUtils.getJobConfig() 方法可以获取调度策略对象。
      * 调度策略可以按照 `秒，分，时，周，月` 进行设置，也可以使用`crontab`表达式进行设置。
      * 调度时间间隔必须大于10分钟。
    * 创建作业
      * 使用 JobUtils.getJob() 方法，传入对应参数（作业名、项目ID、用户名、运行参数、调度策略、依赖文件、组件等），获取作业对象。
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
