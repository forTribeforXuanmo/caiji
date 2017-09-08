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
 * @since 2017-09-08
 */
public class Wbcomment extends Model<Wbcomment> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
	private Long id;
    /**
     * 评论人
     */
	@TableField("USERNAME")
	private String username;
    /**
     * 时间
     */
	@TableField("TIME")
	private String time;
    /**
     * 点赞数
     */
	@TableField("DZNUM")
	private Integer dznum;
    /**
     * 评论内容
     */
	@TableField("CONTENT")
	private String content;
    /**
     * 该微博文章地址
     */
	@TableField("WBPOST_URL")
	private String wbpostUrl;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getDznum() {
		return dznum;
	}

	public void setDznum(Integer dznum) {
		this.dznum = dznum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWbpostUrl() {
		return wbpostUrl;
	}

	public void setWbpostUrl(String wbpostUrl) {
		this.wbpostUrl = wbpostUrl;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Wbcomment{" +
			"id=" + id +
			", username=" + username +
			", time=" + time +
			", dznum=" + dznum +
			", content=" + content +
			", wbpostUrl=" + wbpostUrl +
			"}";
	}
}
