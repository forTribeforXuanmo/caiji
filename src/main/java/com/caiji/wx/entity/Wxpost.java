package com.caiji.wx.entity;

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
 * @since 2017-08-22
 */
public class Wxpost extends Model<Wxpost> {

    private static final long serialVersionUID = 1L;

    /**
     * 内部id
     */
    @TableId("ID")
	private String id;
    /**
     * 所属的公众号
     */
	@TableField("BIZ")
	private String biz;
    /**
     * 微信定义的文章Id
     */
	@TableField("FIELD_ID")
	private String fieldId;
    /**
     * 标题
     */
	@TableField("TITLE")
	private String title;
	@TableField("TITLE_ENCODE")
	private String titleEncode;
    /**
     * 摘要
     */
	@TableField("DIGEST")
	private String digest;
    /**
     * 文章地址
     */
	@TableField("CONTENT_URL")
	private String contentUrl;
    /**
     * 阅读原文地址
     */
	@TableField("SOURCE_URL")
	private String sourceUrl;
    /**
     * 封面图片地址
     */
	@TableField("COVER")
	private String cover;
    /**
     * 是否多图文
     */
	@TableField("IS_MULTI")
	private String isMulti;
    /**
     * 是否头条
     */
	@TableField("IS_TOP")
	private String isTop;
    /**
     * 文章时间戳
     */
	@TableField("DATETIME")
	private String datetime;
	@TableField("READNUM")
	private Integer readnum;
	@TableField("LIKENUM")
	private Integer likenum;
	/**
	 * 文章源链接sn编号
	 */
	private String sn;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBiz() {
		return biz;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleEncode() {
		return titleEncode;
	}

	public void setTitleEncode(String titleEncode) {
		this.titleEncode = titleEncode;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getIsMulti() {
		return isMulti;
	}

	public void setIsMulti(String isMulti) {
		this.isMulti = isMulti;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Integer getReadnum() {
		return readnum;
	}

	public void setReadnum(Integer readnum) {
		this.readnum = readnum;
	}

	public Integer getLikenum() {
		return likenum;
	}

	public void setLikenum(Integer likenum) {
		this.likenum = likenum;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Wxpost{" +
			"id=" + id +
			", biz=" + biz +
			", fieldId=" + fieldId +
			", title=" + title +
			", titleEncode=" + titleEncode +
			", digest=" + digest +
			", contentUrl=" + contentUrl +
			", sourceUrl=" + sourceUrl +
			", cover=" + cover +
			", isMulti=" + isMulti +
			", isTop=" + isTop +
			", datetime=" + datetime +
			", readnum=" + readnum +
			", likenum=" + likenum +
			"}";
	}
}
