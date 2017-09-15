package com.caiji.weibo.listener;

import com.caiji.weibo.WbHandler;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.FalsifyingWebConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Administrator on 2017-9-13.
 * 监听WebClient
 */
public class ConnectionListener extends FalsifyingWebConnection {
    private static final String[] ACCOUNT={"17058395645","17180250119","17085851332","17085850810","17058055294"};
    private static final String[] PASSWORD={"969867yj","393325yj","915623yj","852237yj","678764yj"};
    private static int index =0;
    private WebClient webClient=null;
    private static final Logger LOGGER= LoggerFactory.getLogger(ConnectionListener.class);
    public ConnectionListener(WebClient webClient) throws IllegalArgumentException {
        super(webClient);
        this.webClient=webClient;
    }

    @Override
    public WebConnection getWrappedWebConnection() {
        return super.getWrappedWebConnection();
    }

    @Override
    public WebResponse getResponse(WebRequest request) throws IOException {
        WebResponse response=super.getResponse(request);
        String url=response.getWebRequest().getUrl().toString();
        int statusCode = response.getStatusCode();
        String statusMessage = response.getStatusMessage();
        Set<Cookie> cookies = webClient.getCookies(response.getWebRequest().getUrl());
        LOGGER.info(">>>>>监听  地址"+url+" \n响应码"+statusCode+" 响应信息"+statusMessage);
        for (Cookie cookie:cookies) {
            LOGGER.info(cookie.getName()+" "+cookie.getDomain()+" "+cookie.getExpires()+"  "+cookie.getValue());
        }
        Set<Cookie> cookies2 = webClient.getCookieManager().getCookies();;
        LOGGER.info("---------下面打印cookieManager的------");
        for (Cookie c : cookies2) {
            webClient.getCookieManager().addCookie(c);
            LOGGER.info(c.getName()+" "+c.getDomain()+" "+c.getExpires()+"  "+c.getValue());
        }
        LOGGER.info("<<<<<<<");

            if(!url.startsWith("https://weibo.cn/?featurecode")&&!url.startsWith("http://weibo.cn/?featurecode")){
                if(statusCode==403){
                try {
                    webClient.getCookieManager().clearCookies();
                    LOGGER.info("======= 开始重新登录 ========");
                    WbHandler.login(webClient,ACCOUNT[index],PASSWORD[index]);
                    index=(index+1)%ACCOUNT.length;
                    LOGGER.info("=====已重新登录=======");
                    LOGGER.info("======重新进入url "+url);
                    HtmlPage page = webClient.getPage(url);
                    LOGGER.info("返回的"+url+"数据"+page.asXml());
                    response=page.getWebResponse();
                } catch (InterruptedException e) {
                    LOGGER.info("===尝试重新登录失败！！====",e);
                }
            }
            if(statusCode==302){
                    LOGGER.info("======   302   =======");
                    LOGGER.info("header:"+response.getResponseHeaders());
                    LOGGER.info(response.getContentAsString());

            }



        }
        return response;
    }
}
