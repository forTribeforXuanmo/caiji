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
 * @since 2017-09-08
 */
public class Wbuser extends Model<Wbuser> {

    private static final long serialVersionUID = 1L;

	@TableId(value="ID", type= IdType.AUTO)
	private Integer id;
	@TableField("WEIBONAME")
	private String weiboname;
	@TableField("NEWNAME")
	private String newName;
    /**
     * 简要的描述
     */
	@TableField("COMMENT")
	private String comment;
	@TableField("FANS_NUM")
	private Integer fansNum;
	@TableField("GUANZHU_NUM")
	private Integer guanzhuNum;
	@TableField("WB_NUM")
	private Integer wbNum;
	@TableField("USERID")
	private String userId;
    /**
     * 爬取级别 0，1，2 0表示只爬取微博，1表示包括转发，2表示包括转发和评论
     */
	@TableField("CRAWLEVEL")
	private Integer crawlevel;

	public String getNewName() {
		return newName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWeiboname() {
		return weiboname;
	}

	public void setWeiboname(String weiboname) {
		this.weiboname = weiboname;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getFansNum() {
		return fansNum;
	}

	public void setFansNum(Integer fansNum) {
		this.fansNum = fansNum;
	}

	public Integer getGuanzhuNum() {
		return guanzhuNum;
	}

	public void setGuanzhuNum(Integer guanzhuNum) {
		this.guanzhuNum = guanzhuNum;
	}

	public Integer getWbNum() {
		return wbNum;
	}

	public void setWbNum(Integer wbNum) {
		this.wbNum = wbNum;
	}

	public Integer getCrawlevel() {
		return crawlevel;
	}

	public void setCrawlevel(Integer crawlevel) {
		this.crawlevel = crawlevel;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Wbuser{" +
			"id=" + id +
			", weiboname=" + weiboname +
			", comment=" + comment +
			", fansNum=" + fansNum +
			", guanzhuNum=" + guanzhuNum +
			", wbNum=" + wbNum +
			", crawlevel=" + crawlevel +
			"}";
	}
}
