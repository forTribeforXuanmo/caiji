package com.caiji.wx.mapper;

import com.caiji.wx.entity.Weixin;
import com.caiji.wx.entity.Wxpost;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lsz
 * @since 2017-08-22
 */
public interface WxpostMapper extends BaseMapper<Wxpost> {
   List<Wxpost> selectPostByContentUrl(@Param("contentUrl")String contentUrl);

   int updateNum(@Param("biz") String biz,@Param("sn")String sn,
                 @Param("readNum")Integer readNum,@Param("likeNum")Integer likeNum);

   List<String> selectContentUrlList(@Param("biz")String biz);
}