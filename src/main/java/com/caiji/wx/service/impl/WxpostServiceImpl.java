package com.caiji.wx.service.impl;

import com.caiji.wx.entity.Wxpost;
import com.caiji.wx.mapper.WxpostMapper;
import com.caiji.wx.service.IWxpostService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsz
 * @since 2017-08-22
 */
@Service
public class WxpostServiceImpl extends ServiceImpl<WxpostMapper, Wxpost> implements IWxpostService {

    @Override
    public List<Wxpost> selectPostByContentUrl(String contentUrl) {
        return baseMapper.selectPostByContentUrl(contentUrl);
    }

    @Override
    public int updateNum(String biz,String sn, Integer readNum, Integer likeNum) {
        return baseMapper.updateNum(biz,sn,readNum,likeNum);
    }

    @Override
    public List<String> selectContentUrlList(String biz) {
        return baseMapper.selectContentUrlList(biz);
    }
}
