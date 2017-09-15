package com.caiji.weibo;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.util.Set;

/**
 * Created by Administrator on 2017-9-14.
 */
public class MyCookieManager extends CookieManager {
    @Override
    public synchronized Cookie getCookie(String name) {
        return super.getCookie(name);
    }

    @Override
    public synchronized Set<Cookie> getCookies() {
        return super.getCookies();
    }
}
