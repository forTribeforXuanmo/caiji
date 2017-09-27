package com.caiji.weibo.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lsz
 * @since 2017-09-21
 */
public class Schedulejob extends Model<Schedulejob> {

    private static final long serialVersionUID = 1L;

	@TableId(value="job_id", type= IdType.AUTO)
	private Integer jobId;
    /**
     * 任务名称
     */
	@TableField("job_name")
	private String jobName;
    /**
     * 任务组名
     */
	@TableField("job_group")
	private String jobGroup;
    /**
     * 任务状态 0禁用 1启用 2删除 
     */
	@TableField("job_status")
	private String jobStatus;
    /**
     * cron表达式
     */
	@TableField("cron_expression")
	private String cronExpression;
    /**
     * 任务描述
     */
	@TableField("job_desc")
	private String jobDesc;


	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	@Override
	protected Serializable pkVal() {
		return this.jobId;
	}

	@Override
	public String toString() {
		return "Schedulejob{" +
			"jobId=" + jobId +
			", jobName=" + jobName +
			", jobGroup=" + jobGroup +
			", jobStatus=" + jobStatus +
			", cronExpression=" + cronExpression +
			", jobDesc=" + jobDesc +
			"}";
	}
}
