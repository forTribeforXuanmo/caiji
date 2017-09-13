package com.caiji.weibo.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lsz
 * @since 2017-09-12
 */
public class Wbzhuanfa extends Model<Wbzhuanfa> {

    private static final long serialVersionUID = 1L;

	@TableField("ID")
	private String id;
	@TableField("FROM_UID")
	private String fromUid;
	@TableField("POSTID")
	private String postid;
	@TableField("CONTENT")
	private String content;
	@TableField("FROM_NAME")
	private String fromName;
	@TableField("TIME")
	private String time;
	@TableField("POSTURL")
	private String posturl;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromUid() {
		return fromUid;
	}

	public void setFromUid(String fromUid) {
		this.fromUid = fromUid;
	}

	public String getPostid() {
		return postid;
	}

	public void setPostid(String postid) {
		this.postid = postid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPosturl() {
		return posturl;
	}

	public void setPosturl(String posturl) {
		this.posturl = posturl;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Wbzhuanfa{" +
			"id=" + id +
			", fromUid=" + fromUid +
			", postid=" + postid +
			", content=" + content +
			", fromName=" + fromName +
			", time=" + time +
			", posturl=" + posturl +
			"}";
	}
}
