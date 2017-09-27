package com.caiji.quartze;

import com.caiji.weibo.entity.Schedulejob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 2017-9-21.
 * 与spring 结合的任务调度管理
 */
@Component
public class JobManage {

    public static final String DEFAULT_JOB_KEY = "scheduleJob";

    private static final Logger LOGGER = LoggerFactory.getLogger(JobManage.class);
    @Autowired
    private ISchedulejobService scheduleJobService;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private Scheduler scheduler;

    @PostConstruct
    public void initSpringDI() {
        System.out.println("构造完后在赋值");
        scheduler = schedulerFactoryBean.getScheduler();
    }

    public void initTask() {
        List<Schedulejob> scheduleJobList = scheduleJobService.selectList(null); //查询数据库的任务
        for (Schedulejob schedulejob : scheduleJobList) {
            addJob(schedulejob);
        }
    }

    /**
     * 添加任务
     *
     * @param job
     */
    public void addJob(Schedulejob job) {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (null == trigger) {
                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getJobName(), job.getJobGroup()).build();
                jobDetail.getJobDataMap().put(DEFAULT_JOB_KEY, job);
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                // Trigger已存在，那么更新相应的定时设置
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                // 按新的cronExpression表达式重新构建trigger
                TriggerBuilder triggerBuilder = trigger.getTriggerBuilder();
                trigger = triggerBuilder.withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }

            if ("0".equals(job.getJobStatus())) {
                pauseJob(job);
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停任务
     *
     * @param job
     */
    public void pauseJob(Schedulejob job) {
        LOGGER.info("暂停任务", job.getJobName());
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            LOGGER.info("暂停任务失败", e);
        }
    }

    /**
     * 恢复一个job
     *
     * @param scheduleJob
     */
    public void resumeJob(Schedulejob scheduleJob) {
        LOGGER.info(scheduleJob.getJobName(), "定时任务启动");
        //Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除一个任务
     *
     * @param job
     */
    public void removeJob(Schedulejob job) {
        LOGGER.info(job.getJobName(), "删除定时任务");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        // 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        //CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
