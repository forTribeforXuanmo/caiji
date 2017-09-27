package com.caiji.quartze;

import com.caiji.util.BeanUtil;
import com.caiji.weibo.entity.Schedulejob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017-9-21.
 *
 * Job工厂
 */
@DisallowConcurrentExecution
public class QuartzJobFactory  implements Job{

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobFactory.class);
    @Override
    public void execute(JobExecutionContext context){
        try {
            Schedulejob scheduleJob = (Schedulejob) context.getMergedJobDataMap().get(JobManage.DEFAULT_JOB_KEY);
            LOGGER.info(scheduleJob.getJobName(),"任务开始");
            ((ISchedulejobService)BeanUtil.getBean(scheduleJob.getJobName())).execute();
            LOGGER.info(scheduleJob.getJobName(),"任务结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
