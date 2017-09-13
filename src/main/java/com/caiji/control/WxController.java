package com.caiji.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.caiji.wx.entity.Weixin;
import com.caiji.wx.entity.Wxpost;
import com.caiji.wx.service.IWeixinService;
import com.caiji.wx.service.IWxpostService;
import com.caiji.wx.service.IWxtmplistService;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("wx")
public class WxController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WxController.class);
    /**
     * 已经抓取的文章的链接
     */
    private static List<String> VisitContentUrl = new ArrayList<>();
    /**
     * 当前抓取的公众号首页地址
     */
    private static volatile String NowBizIndexUrl = "";
    /**
     * 当前biz
     */
    private static String NowBiz = "";


    /**
     * 当前偏移
     */
    private static Integer Offset = 10;

    /**
     * false表示正常偏移
     * True表示退出后继续上次偏移
     */
    private static Boolean OffsetException = false;

    /**
     * 当前PassTicket
     */
    private static String NowPassTicket = "";
    /**
     * 需要抓取的微信公众号
     */
    private static List<Weixin> WeixinList = null;
    /**
     * 一个公众号中的文章数
     */
    private static Integer OneBizTopicCount = 0;

    @Autowired
    private IWeixinService weixinService;

    @Autowired
    private IWxpostService wxpostService;

    @Autowired
    private IWxtmplistService wxtmplistService;


    @RequestMapping(value = "getMsgJson", method = RequestMethod.POST)
    @ResponseBody
    public String getMsg(HttpServletRequest request) {

        try {
            return getMsgJson(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";

    }

    public synchronized String getMsgJson(HttpServletRequest request) throws UnsupportedEncodingException {

        String str = StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeHtml(URLDecoder.decode(request.getParameter("str"), "utf-8")));
        String url = URLDecoder.decode(request.getParameter("url"));
        String respData = request.getParameter("respData");
        logger.info(url);
        logger.info(str);
        Map mapUrl = handleUrl(url);
        logger.info("url map:\n");
        logger.info(JSON.toJSONString(mapUrl));
        String action = (String) mapUrl.get("action");
        String wxHeader = (String) mapUrl.get("wx_header");
        NowBiz = (String) mapUrl.get("__biz");

        if ("home".equals(action) && "1".equals(wxHeader)) {
            NowPassTicket = (String) mapUrl.get("pass_ticket");
            //进入首页正常情况是文章数清零
            if (OneBizTopicCount != 0) {
                logger.warn("======不正常进入============");
                if (VisitContentUrl.size() == 0) {
                    OneBizTopicCount = 0;
                    //文章历史页面异常，后退重新进入了公众号文章首页
                    //直接在首页跳转继续进入上次的公众号，当进入公主号的时候，接着上次偏移继续
                    OffsetException = true;
                    return "new:" + "https://mp.weixin.qq.com" + NowBizIndexUrl;
                } else {
                    //在进入文章的时候异常,后退重新进入了公众号文章首页
                    //继续进入上次的文章
                    String nextContentUrl = VisitContentUrl.get(0);
                    String wxTopicUrl = "new:" + nextContentUrl.substring(0, nextContentUrl.indexOf("scene")) + "scene=38" + "&pass_ticket=" + NowPassTicket;
                    return wxTopicUrl;
                }
            } else {
                logger.info("===========第一页 =======================");
                if (url.equals(NowBizIndexUrl)) {
                    logger.warn("#########连续重复进入首页+" + url + "！！########");
                } else {
                    NowBizIndexUrl = url;
                }
            }
        }
        if (WeixinList == null || WeixinList.size() == 0) {
            WeixinList = weixinService.selectList(null);
            if (WeixinList.size() == 0 || WeixinList == null) {
                throw new RuntimeException("微信表中没有数据");
            }
        }
        //公众号biz
        final String biz = (String) mapUrl.get("__biz");

        if (respData != null) {
            Document historyDocument = Jsoup.parse(respData);

            //公众号昵称
            String nickname = historyDocument.select("#nickname").text();

            //公众号描述
            String desc = historyDocument.select(".profile_desc").text();

            //公众号头像
            String headImg = historyDocument.select("#icon").attr("src");
            Map mapWhere = new HashMap(3);
            mapWhere.put("biz", biz);
            List<Weixin> weiXinList = weixinService.selectByMap(mapWhere);
            if (weiXinList == null || weiXinList.size() == 0) {
                Weixin newWeiXin = new Weixin();
                newWeiXin.setId(String.valueOf(IdWorker.getId()));
                newWeiXin.setBiz(biz);
                newWeiXin.setDesc(desc);
                newWeiXin.setName(nickname);
                newWeiXin.setImg(headImg);
                newWeiXin.setTimeStamp(System.currentTimeMillis());
                weixinService.insert(newWeiXin);

            }
        }

        /****读取文章列表数据********/
        Map<String, Object> json = JSON.parseObject(str, HashMap.class);
        List<Map<String, Object>> list = (List<Map<String, Object>>) json.get("list");
        for (Map<String, Object> map : list) {
            //第一部分
            Map<String, Object> comm_msg_info = (Map<String, Object>) map.get("comm_msg_info");
            //第二部分
            Map<String, Object> app_msg_ext_info = (Map<String, Object>) map.get("app_msg_ext_info");

            Integer type = (Integer) comm_msg_info.get("type");
            if (type == 49) {//图文消息
                String content_url = ((String) app_msg_ext_info.get("content_url")).replaceAll("\\\\", "");
                List<Wxpost> wxpostList = wxpostService.selectPostByContentUrl(content_url);
                //多图文
                String is_multi = String.valueOf(app_msg_ext_info.get("is_multi"));
                //一组文章的时间
                long datetime = (long) ((int) comm_msg_info.get("datetime"));

                if (wxpostList == null || wxpostList.size() == 0) {
                    //排除为空的情况，为空的不写入数据库
                    if (!"".equals(content_url)) {
                        //没有则写入第一条以及该组下面的其他多图文文章
                        String fieldId = String.valueOf(app_msg_ext_info.get("fileid"));
                        String title = (String) app_msg_ext_info.get("title");
                        String title_encode = URLEncoder.encode(title.replaceAll("&nbsp;", ""));
                        String digest = (String) app_msg_ext_info.get("digest");
                        String source_url = ((String) app_msg_ext_info.get("source_url")).replaceAll("\\\\", "");
                        String cover = ((String) app_msg_ext_info.get("cover")).replaceAll("\\\\", "");
                        Map source_url_map=handleUrl(content_url);
                        String sn= (String) source_url_map.get("sn");
                        if("".equals(sn)||sn==null){
                            sn= (String) source_url_map.get("sign");
                        }
                        String is_top = "1";
                        Wxpost wxpost = new Wxpost();
                        wxpost.setId(String.valueOf(IdWorker.getId()));
                        wxpost.setBiz(biz);
                        wxpost.setFieldId(fieldId);
                        wxpost.setTitle(title);
                        wxpost.setTitleEncode(title_encode);
                        wxpost.setDigest(digest);
                        wxpost.setContentUrl(content_url);
                        wxpost.setSourceUrl(source_url);
                        wxpost.setCover(cover);
                        wxpost.setIsMulti(is_multi);
                        wxpost.setIsTop(is_top);
                        wxpost.setDatetime(datetime);
                        wxpost.setSn(sn);
                        wxpostService.insert(wxpost);
                        OneBizTopicCount++;
                        logger.info("新增文章" + title + "OneBizTopicCount" + OneBizTopicCount);
                    }
                } else {
                    logger.info("文章已存在url，不插入到数据库: " + content_url);
                }
                //取这一组内部的文章
                if (is_multi.equals("1")) {
                    List<Map> multi_app_msg_item_list = (List<Map>) app_msg_ext_info.get("multi_app_msg_item_list");
                    for (Map item : multi_app_msg_item_list) {
                        String contentUrl = ((String) item.get("content_url")).replaceAll("\\\\", "");
                        List<Wxpost> wxpostItemList = wxpostService.selectPostByContentUrl(contentUrl);
                        if (wxpostItemList == null || wxpostItemList.size() == 0) {
                            if (!"".equals(contentUrl)) {
                                String fieldId = String.valueOf(item.get("fileid"));
                                String title = (String) item.get("title");
                                String title_encode = URLEncoder.encode(title.replaceAll("&nbsp;", ""));
                                String digest = (String) item.get("digest");
                                String source_url = ((String) item.get("source_url")).replaceAll("\\\\", "");
                                String cover = ((String) app_msg_ext_info.get("cover")).replace("\\\\", "");
                                String is_top = "0";
                                Map source_url_map=handleUrl(contentUrl);
                                String sn= (String) source_url_map.get("sn");
                                if(sn==null||"".equals(sn)){
                                    sn= (String) source_url_map.get("sign");
                                }
                                Wxpost wxpost = new Wxpost();
                                wxpost.setId(String.valueOf(IdWorker.getId()));
                                wxpost.setBiz(biz);
                                wxpost.setFieldId(fieldId);
                                wxpost.setTitle(title);
                                wxpost.setTitleEncode(title_encode);
                                wxpost.setDigest(digest);
                                wxpost.setContentUrl(contentUrl);
                                wxpost.setSourceUrl(source_url);
                                wxpost.setCover(cover);
                                wxpost.setIsMulti(is_multi);
                                wxpost.setIsTop(is_top);
                                wxpost.setDatetime(datetime);
                                wxpost.setSn(sn);
                                wxpostService.insert(wxpost);
                                OneBizTopicCount++;
                                logger.info("新增文章" + title + "OneBizTopicCount" + OneBizTopicCount);
                            }
                        } else {
                            logger.info("文章已存在url，不插入到数据库: " + content_url);
                        }
                    }
                }
            }
        }
        /** end 读取完list集合中的文章列表，无论是第一页的数据还是后面翻页后得到的数据   end**/


        if ("home".equals(action) && "1".equals(wxHeader)) {
            //微信的网页上定时获取下一页的文章
            String passTicket = (String) mapUrl.get("pass_ticket");
            String page = "https://mp.weixin.qq.com/mp/profile_ext?" +
                    "action=getmsg&__biz=" + biz +
                    "&f=json&offset=_offset&OneBizTopicCount=10&is_ok=1&scene=124&uin" +
                    "=777&key=777&pass_ticket=" + passTicket + "&wxtoken=";
            if (OffsetException) {
                page = "OFFSET=" + Offset + "##" + page;
                OffsetException = false;
                logger.info("继续上次偏移" + Offset);
            }
            return page;
        } else if ("getmsg".equals(action)) {
            if (list.size() == 0 || list == null) {
                //已经查询完公众号全部文章，再一个个进入VisitContentUrl中的文章（获取点赞数和阅读数），最后跳转到下一个Biz地址
                VisitContentUrl = wxpostService.selectContentUrlList(NowBiz);
                if(VisitContentUrl.size()==0){
                    //该公众号没有文章，直接取下一个
                    String nextBizUrl="new:https://mp.weixin.qq.com" + getNextBizUrl();
                    return nextBizUrl;
                }

                String nextContentUrl = VisitContentUrl.get(0);
                String wxTopicUrl = "new:" + nextContentUrl.substring(0, nextContentUrl.indexOf("scene")) + "scene=38" + "&pass_ticket=" + NowPassTicket;
                OffsetException = false;
                logger.info("===准备第一次进入文章 url（" + wxTopicUrl + ")=========");
                return wxTopicUrl;
            } else {
                Offset = Integer.parseInt((String) mapUrl.get("offset"));
                logger.info("还有翻页数据，没有进入文章");
            }
        }
        return "";
    }

    /**
     * 获取下一个BIz
     *
     * @return
     */
    private String getNextBiz() {
        WeixinList.remove(new Weixin(NowBiz));
        if (WeixinList == null || WeixinList.size() == 0) {
            logger.info("#############已经抓取完一轮,暂停10秒，继续下一轮############");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WeixinList = weixinService.selectList(null);
        }
        String nextBiz = WeixinList.get(0).getBiz();
        if (nextBiz.equals(NowBiz)) {
            nextBiz = WeixinList.get(1).getBiz();
        }
        return nextBiz;
    }


    /**
     * 获取下个公众号的url
     *
     * @return
     */
    public String getNextBizUrl() {
        String nextBiz = getNextBiz();
        String nextBizUrl = NowBizIndexUrl.replaceAll("__biz=(.*?)&", "__biz=" + nextBiz + "&");
        return nextBizUrl;
    }

    /**
     * 获取阅读数和点赞数
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "getMsgExt", method = RequestMethod.POST)
    @ResponseBody
    public String getMsgExt(HttpServletRequest request) throws UnsupportedEncodingException {
        String str = request.getParameter("str");
        String url = request.getParameter("url");
        str = URLDecoder.decode(str, "utf-8");
        url = URLDecoder.decode(url, "utf-8");
        logger.info("ext的str为"+str);
        JSONObject appMsgExt = JSON.parseObject(str);
        logger.info("ext的str转map为"+appMsgExt);
        Map appMsgStat = (Map) appMsgExt.get("appmsgstat");
        //点赞数和阅读数
        Integer readNum = (Integer) appMsgStat.get("read_num");
        Integer likeNum = (Integer) appMsgStat.get("like_num");
        Map map = handleUrl(url);
        String biz = (String) map.get("__biz");
        String sn = (String) map.get("sn");
        if("".equals(sn)||sn==null){
            sn= (String) map.get("sign");
        }
        logger.info("======= <获取地址为(" + url + ")的阅读数" + readNum + "点赞数" + likeNum + "> ======== ");
        wxpostService.updateNum(biz, sn, readNum, likeNum);
        //删除当前的contentUrl
        for (String tmp : VisitContentUrl) {
            if (tmp.indexOf("__biz=" + biz) != -1 && tmp.indexOf("sn=" + sn) != -1) {
                VisitContentUrl.remove(tmp);
                logger.info("==========队列中删除对应的文章链接(" + tmp + ")======");
                break;
            }
        }
        return "success:" + str;
    }

    /**
     * URL地址参数转Map
     *
     * @param url
     * @return
     */
    private Map handleUrl(String url) {
        Map map = new HashMap();
        String[] strings = url.split("\\?", 2);
        String action = strings[0];
        map.put("URL_ACTION", action);
        String params = strings[1];
        String[] paramSplit = params.split("&");
        for (String param : paramSplit) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                if (map.containsKey(keyValue[0])) {
                    String groupValue = map.get(keyValue[0]) + "," + keyValue[1];
                    map.put(keyValue[0], groupValue);
                } else {
                    map.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return map;
    }

    @RequestMapping(value = "getWxHis", method = RequestMethod.GET)
    @ResponseBody
    public String getWxHis() {
        System.out.println("getWxHis，暂时不做操作");
        return "";
    }

    /**
     * 进入微信文章
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getWxPost")
    @ResponseBody
    public String getWxPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        logger.info("==========进入微信文章==========");
        String url=URLDecoder.decode(request.getParameter("url"),"utf-8");
        logger.info("文章地址："+url);
        String nextUrl = "";
        if (VisitContentUrl.size() > 1) {
            String nextContentUrl = VisitContentUrl.get(1);
            //如果第二个和当前url一样，说明之前文章没有获取到阅读量信息，也就意味着该条文章被删除了
            if(nextContentUrl.equals("http://mp.weixin.qq.com"+url)){
                VisitContentUrl.remove(0);
                nextContentUrl=VisitContentUrl.get(1);
                logger.info("文章被删除了,从队列移除第一个，重新取第二个");
            }
            nextUrl = nextContentUrl.substring(0, nextContentUrl.indexOf("scene")) + "scene=38" + "&pass_ticket=" + NowPassTicket;
            logger.info("==进入文章后下一篇文章地址===" + nextUrl);
        } else {
            //进入下一个公众号，VisitContentUrl清空,历史文章数清零
            nextUrl = "https://mp.weixin.qq.com" + getNextBizUrl();
            VisitContentUrl.clear();   //清除文章队列
            OneBizTopicCount = 0;
            Offset = 10; //偏移量重置
        }
        String script = "<script>setTimeout(function() {window.location.href='" + nextUrl +
                "';  },5000);  </script>";
        return script;
    }


    @RequestMapping("/test")
    //@ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        System.out.println("asdasd");
        response.getWriter().write("new:www.baidu.com");

    }


}
