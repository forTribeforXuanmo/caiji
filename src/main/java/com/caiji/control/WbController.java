package com.caiji.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.caiji.weibo.entity.Wbpost;
import com.caiji.weibo.entity.Wbuser;
import com.caiji.weibo.service.IWbpostService;
import com.caiji.weibo.service.IWbuserService;
import com.caiji.weibo.service.WbHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-9-11.
 */
@Controller
@RequestMapping("/wb")
public class WbController {
    private static final Logger LOGGER= LoggerFactory.getLogger(WbController.class);
    @Autowired
    private WbHandler wbHandler;
    @Autowired
    private IWbuserService wbuserService;

    @Autowired
    private IWbpostService wbpostService;

    @RequestMapping("/index")
    public String index(){
        LOGGER.info("首页进入");
        return "index";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login() throws IOException, InterruptedException {
        WebClient client=wbHandler.getClient();
        wbHandler.login(client);
        return "ok";
    }


    @RequestMapping("/start")
    @ResponseBody
    public String start(HttpServletResponse response) throws IOException, InterruptedException {
        WebClient client = wbHandler.getClient();
        wbHandler.login(client);
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
        wbHandler.login(client);
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
            wbHandler.login(client);
            s = wbHandler.saveAllWeiboComment(client);
        } catch (Exception e) {
            s=s+"发生异常"+e;
        }finally {
            client.close();
            return s;
        }
    }

}
