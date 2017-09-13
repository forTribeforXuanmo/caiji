package com.caiji.weibo.service;

import com.caiji.weibo.entity.Wbuser;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsz
 * @since 2017-09-08
 */
public interface IWbuserService extends IService<Wbuser> {
	List<Wbuser> getZhuanfaUserList();
}
