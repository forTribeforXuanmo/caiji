package com.caiji.quartze;

import com.caiji.weibo.WbHandler;
import com.caiji.weibo.entity.Schedulejob;
import com.caiji.weibo.mapper.SchedulejobMapper;
import com.caiji.quartze.ISchedulejobService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gargoylesoftware.htmlunit.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsz
 * @since 2017-09-21
 */
@Service("wbJob_No1")
public class WbJobNo1ServiceImpl extends ServiceImpl<SchedulejobMapper, Schedulejob> implements ISchedulejobService {
    @Autowired
    private WbHandler wbHandler;
    @Override
    public void execute() {
        WebClient webClient=wbHandler.getClient();
        try {
            WbHandler.login(webClient, "15896264186", "lishengzhu");
            wbHandler.saveAllWeiboTopic(webClient);
            wbHandler.saveAllWeiboZhuanfa(webClient);
            wbHandler.saveAllWeiboComment(webClient);
            webClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
