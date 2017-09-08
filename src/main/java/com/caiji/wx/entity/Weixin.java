package com.caiji.wx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 * 公众号的Id列表
 * </p>
 *
 * @author lsz
 * @since 2017-08-22
 */
public class Weixin extends Model<Weixin> {

    private static final long serialVersionUID = 1L;

    /**
     * 内部id
     */
    @TableId("ID")
    private String id;
    /**
     * 公众号的id
     */
    @TableField("BIZ")
    private String biz;
    /**
     * 采集时间戳
     */
    @TableField("TIME_STAMP")
    private Long timeStamp;

    @TableField("NAME")
    private String name;

    @TableField("IMG")
    private String img;

    @TableField("DESC")
    private String desc;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    public Weixin(){}
    public Weixin(String biz) {
        this.biz = biz;
    }

    @Override
    public String toString() {
        return "Weixin{" +
                "id='" + id + '\'' +
                ", biz='" + biz + '\'' +
                ", timeStamp=" + timeStamp +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weixin weixin = (Weixin) o;
        if(biz!=null && (weixin.getBiz()!=null)){
            if(((Weixin) o).getBiz().equals(biz)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (biz != null ? biz.hashCode() : 0);
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}
