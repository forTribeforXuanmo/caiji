package com.caiji.quartze;

import com.caiji.weibo.WbHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2017-9-19.
 */

@DisallowConcurrentExecution
public class WbCrawlJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        WbHandler wbHandler=new WbHandler();
        WebClient webClient = wbHandler.getClient();
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
