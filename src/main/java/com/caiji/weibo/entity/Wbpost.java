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
public class Wbpost extends Model<Wbpost> {

    private static final long serialVersionUID = 1L;

    /**
     * 内部Id
     */
    @TableId("ID")
	private String id;
    /**
     * 发布内容html代码
     */
	@TableField("CONTENT")
	private String content;
    /**
     * 发布时间
     */
	@TableField("TIME")
	private String time;
    /**
     * 转发数
     */
	@TableField("ZHUANFA_NUM")
	private Integer zhuanfaNum;
    /**
     * 评论数
     */
	@TableField("PINGLUN_NUM")
	private Integer pinglunNum;
    /**
     * 点赞数
     */
	@TableField("DIANZAN_NUM")
	private Integer dianzanNum;
    /**
     * url地址
     */
	@TableField("URL")
	private String url;
    /**
     * 微博用户
     */
	@TableField("WEIBONAME")
	private String weiboname;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getZhuanfaNum() {
		return zhuanfaNum;
	}

	public void setZhuanfaNum(Integer zhuanfaNum) {
		this.zhuanfaNum = zhuanfaNum;
	}

	public Integer getPinglunNum() {
		return pinglunNum;
	}

	public void setPinglunNum(Integer pinglunNum) {
		this.pinglunNum = pinglunNum;
	}

	public Integer getDianzanNum() {
		return dianzanNum;
	}

	public void setDianzanNum(Integer dianzanNum) {
		this.dianzanNum = dianzanNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWeiboname() {
		return weiboname;
	}

	public void setWeiboname(String weiboname) {
		this.weiboname = weiboname;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Wbpost{" +
			"id=" + id +
			", content=" + content +
			", time=" + time +
			", zhuanfaNum=" + zhuanfaNum +
			", pinglunNum=" + pinglunNum +
			", dianzanNum=" + dianzanNum +
			", url=" + url +
			", weiboname=" + weiboname +
			"}";
	}
}
