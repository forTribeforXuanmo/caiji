package com.caiji.wx.service;

import com.caiji.wx.entity.Wxpost;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsz
 * @since 2017-08-22
 */
public interface IWxpostService extends IService<Wxpost> {
    List<Wxpost> selectPostByContentUrl(String contentUrl);
    int updateNum( String biz,String sn,Integer readNum,Integer likeNum);
    List<String> selectContentUrlList(@Param("biz")String biz);
}
