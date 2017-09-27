package com.caiji.control;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.caiji.quartze.JobManage;
import com.caiji.quartze.QuartzManager;
import com.caiji.weibo.entity.Schedulejob;
import com.caiji.weibo.entity.Wbpost;
import com.caiji.quartze.ISchedulejobService;
import com.caiji.weibo.service.IWbpostService;
import com.caiji.weibo.service.IWbuserService;
import com.caiji.weibo.WbHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017-9-11.
 */
@Controller
@RequestMapping("/wb")
public class WbController {
    private static final Logger LOGGER= LoggerFactory.getLogger(WbController.class);

    @Autowired
    private IWbuserService wbuserService;

    @Autowired
    private IWbpostService wbpostService;

    @Autowired
    private ISchedulejobService schedulejobService;

    @Autowired
    private JobManage jobManage;

    @Autowired
    private WbHandler wbHandler;

    @RequestMapping("/index")
    public String index(){
        LOGGER.info("首页进入");
        return "index";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login() throws IOException, InterruptedException {

        WebClient client=wbHandler.getClient();
        wbHandler.login(client,"15896264186","lishengzhu");
        return "ok";
    }

    @RequestMapping("/loginpc")
    public String loginPC() throws IOException, InterruptedException {

        WebClient client=wbHandler.getClient();
        wbHandler.login_pc(client,"17085851332","915623yj");
        return "success";
    }


    @RequestMapping("/startTopic")
    @ResponseBody
    public String start(HttpServletResponse response) throws IOException, InterruptedException {

        WebClient client = wbHandler.getClient();
        wbHandler.login(client,"15896264186","lishengzhu");
        String msg="";
        try {
            msg=wbHandler.saveAllWeiboTopic(client);
        } catch (Exception e) {
            msg=msg+"==发生错误转化异常==="+e;

        }finally {
            client.close();
            return msg;
        }
        /*****/

    }


    @RequestMapping("/getWbPost")
    @ResponseBody
    public List<Wbpost> getWbPost(){
        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("WBUSERTABLE_ID");
        ew.orderBy("TIME",false);
        List<Wbpost> list=wbpostService.selectList(ew);
        return list;
    }

    @RequestMapping("/startZhuanfa")
    @ResponseBody
    public String startZhuanfa() throws IOException, InterruptedException {

        WebClient client=wbHandler.getClient();
        wbHandler.login(client,"15896264186","lishengzhu");
        String s = null;
        try {
            s = wbHandler.saveAllWeiboZhuanfa(client);
        } catch (Exception e) {
            s=s+"发生异常"+e;
        }finally {
            client.close();
            return s;
        }
    }

    @RequestMapping("/startComment")
    @ResponseBody
    public String startComment(){

        WebClient client=wbHandler.getClient();
        String s = null;
        try {
            wbHandler.login(client,"15896264186","lishengzhu");
            s = wbHandler.saveAllWeiboComment(client);
        } catch (Exception e) {
            s=s+"发生异常"+e;
        }finally {
            client.close();
            return s;
        }
    }

    @RequestMapping("/startQuartz")
    @ResponseBody
    public String startQuartz(){
        Schedulejob schedulejob=new Schedulejob();
        schedulejob.setJobId(2);
        schedulejob.setJobName("wbJob_No1");
        schedulejob.setJobGroup("lsz");
        schedulejob.setJobStatus("1");
        schedulejob.setCronExpression("* * 0/2 * * ?".trim());
        jobManage.addJob(schedulejob);
        return "success:";
    }
    @RequestMapping("/stopQuartz")
    @ResponseBody
    public String stopQuzrtz(){
        String job_name="新浪微博采集";
        QuartzManager.shutdownJobs();
        LOGGER.info("【任务已停止】...");
        return "success";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "success";
    }

    @RequestMapping("/showJob")
    @ResponseBody
    public Map showJob(){
        List<Schedulejob> schedulejobs = schedulejobService.selectList(null);
        Map  map=new HashMap();
        map.put("data",schedulejobs);
        return map;
    }
}
