package com.caiji.weibo;

import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;

/**
 * author: lishengzhu
 * eamil:530735771@qq.com
 * date:2017/9/17
 * time:21:32
 */
public class WeiboTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        WbHandler handler=new WbHandler();
        WebClient client=handler.getClient();
        handler.login_pc(client,"17085851332","915623yj");
    }
}
