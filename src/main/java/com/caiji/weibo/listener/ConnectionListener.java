package com.caiji.weibo.listener;

import com.caiji.weibo.service.WbHandler;
import com.gargoylesoftware.htmlunit.*;
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
        LOGGER.info(">>>>>监听  地址"+url+" 响应码"+statusCode+" 响应信息"+statusMessage);
        for (Cookie cookie:cookies) {
            LOGGER.info(cookie.getName()+" "+cookie.getDomain()+" "+cookie.getExpires()+"  "+cookie.getValue());
        }
        LOGGER.info("<<<<<<<");
        if(statusCode==403){
            try {
                WbHandler.login(webClient);
                LOGGER.info("=====已重新登录=====");
                Page page = webClient.getPage(url);
                response=page.getWebResponse();

            } catch (InterruptedException e) {
                LOGGER.info("===尝试重新登录失败！！====",e);
            }
        }
        return response;
    }
}
