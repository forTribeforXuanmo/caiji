package com.caiji.weibo.mapper;

import com.caiji.weibo.entity.Wbuser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lsz
 * @since 2017-09-08
 */
public interface WbuserMapper extends BaseMapper<Wbuser> {
    List<Wbuser> getZhuanfaUserList();
}