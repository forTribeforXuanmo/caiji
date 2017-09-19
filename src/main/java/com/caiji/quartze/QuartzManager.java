package com.caiji.quartze;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by Administrator on 2017-9-19.
 */
public class QuartzManager {
    private static SchedulerFactory SchedulerFactory=new StdSchedulerFactory();
    private static String JOB_GROUP_NAME="WBCAIJI_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME="WBCAIJI_TRIGGERGROUP_NAME";

    public static void addJob(String jobName,Class cls,String cronTime){
        try {
            Scheduler scheduler=SchedulerFactory.getScheduler();

            //任务名，任务组，执行类
            JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
            JobDetail jobDetail=JobBuilder.newJob(cls).withIdentity(jobKey).build();
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobName, TRIGGER_GROUP_NAME)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronTime))
                    .build();
            if(!scheduler.isShutdown()){
                scheduler.start();
                scheduler.scheduleJob(jobDetail,trigger);
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    public static void modifyJobTime(String jobName,String cronTime){
        try {
            Scheduler scheduler=SchedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME));
            if(trigger==null){
                return;
            }
            String oldTime = trigger.getCronExpression();
            if(!oldTime.equalsIgnoreCase(cronTime)){
                JobDetail jobDetail=scheduler.getJobDetail(JobKey.jobKey(jobName,JOB_GROUP_NAME));
                Class cls=jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName,cls,cronTime);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler sched = SchedulerFactory.getScheduler();
            sched.pauseTrigger(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME));// 停止触发器
            sched.unscheduleJob(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME));// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, JOB_GROUP_NAME));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = SchedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = SchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
