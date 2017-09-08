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
public class Wxtmplist extends Model<Wxtmplist> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
	private String id;
	@TableField("CONTENT_URL")
	private String contentUrl;
	@TableField("LOAD")
	private String load;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Wxtmplist{" +
			"id=" + id +
			", contentUrl=" + contentUrl +
			", load=" + load +
			"}";
	}
}
