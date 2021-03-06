package com.caiji.weibo.entity;

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
 * @since 2017-09-13
 */
public class Wbcomment extends Model<Wbcomment> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
	private String id;
    /**
     * 评论人
     */
	@TableField("FROM_NAME")
	private String fromName;
    /**
     * 微博系统的id
     */
	@TableField("FROM_UID")
	private String fromUid;
    /**
     * 时间
     */
	@TableField("TIME")
	private String time;
    /**
     * 文章id
     */
	@TableField("POSTID")
	private String postid;
    /**
     * 评论内容
     */
	@TableField("CONTENT")
	private String content;
    /**
     * 该微博文章地址
     */
	@TableField("POSTURL")
	private String posturl;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromUid() {
		return fromUid;
	}

	public void setFromUid(String fromUid) {
		this.fromUid = fromUid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
		return "Wbcomment{" +
			"id=" + id +
			", fromName=" + fromName +
			", fromUid=" + fromUid +
			", time=" + time +
			", postid=" + postid +
			", content=" + content +
			", posturl=" + posturl +
			"}";
	}
}
