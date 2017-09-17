package com.caiji.weibo;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.caiji.weibo.entity.Wbcomment;
import com.caiji.weibo.entity.Wbpost;
import com.caiji.weibo.entity.Wbuser;
import com.caiji.weibo.entity.Wbzhuanfa;
import com.caiji.weibo.listener.ConnectionListener;
import com.caiji.weibo.service.IWbcommentService;
import com.caiji.weibo.service.IWbpostService;
import com.caiji.weibo.service.IWbuserService;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Administrator on 2017-9-4.
 */
@Component
public class WbHandler {

    @Autowired
    private IWbuserService wbuserService;
    @Autowired
    private IWbpostService wbpostService;
    @Autowired
    private IWbcommentService wbcommentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WbHandler.class);
    private final String pattern[] = {"yyyy-MM-dd HH:mm:ss"};

    /**
     * 获取调整参数了的Client实例
     *
     * @return
     */
    public WebClient getClient() {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(100000);
        new ConnectionListener(webClient);   //监听
        return webClient;
    }

    public static void start() throws Exception {

    }


    /**
     * 登录
     *
     * @param client
     * @return
     * @throws IOException
     */
    public static HtmlPage login(WebClient client,String account,String password) throws IOException, InterruptedException {

        HtmlPage page = client.getPage("https://passport.weibo.cn/signin/login?entry=mweibo&r=http%3A%2F%2Fweibo.cn%2F%3Ffeaturecode%3D20000320%26luicode%3D10000011%26lfid%3D100103type%253D1%2526q%253DEMINEM%25E4%25B8%25AD%25E6%2596%2587%25E7%25BD%2591%25E5%25AE%2598%25E6%2596%25B9%25E5%25BE%25AE%25E5%258D%259A&backTitle=%CE%A2%B2%A9&vt=");
        HtmlInput userName = (HtmlInput) page.getElementById("loginName");
        HtmlInput pwd = (HtmlInput) page.getElementById("loginPassword");
        HtmlAnchor submit = (HtmlAnchor) page.getElementById("loginAction");
        userName.setValueAttribute(account);
        pwd.setValueAttribute(password);
        HtmlPage myPage = submit.click();
        while (myPage == null) {
            Thread.sleep(1000);
        }

        LOGGER.info("####### 我的微博首页移动端 ####\n" + myPage.asXml());
        return myPage;
    }
    public static void login_pc(WebClient client,String account,String password) throws IOException, InterruptedException {

        HtmlPage page = client.getPage("https://login.sina.com.cn");
        Thread.sleep(1000);
        LOGGER.info("pc端"+page.asXml());
        HtmlInput userName = (HtmlInput) page.getElementById("username");
        HtmlInput pwd = (HtmlInput) page.getElementById("password");
        Thread.sleep(500);

        userName.setValueAttribute(account);
        pwd.setValueAttribute(password);
        HtmlInput submit =page.querySelector("input[type='submit']");

        HtmlPage click = submit.click();

        HtmlImage vcode_img= (HtmlImage) click.getElementById("check_img");
        if(vcode_img!=null){
            String src=vcode_img.getSrcAttribute();
            if(!"about:blank".equals(src)){
                downloadImg(vcode_img.getImageReader());
                //TODO 处理验证码

            }
        }


//
//
//        userName.setValueAttribute(account);
//        pwd.setValueAttribute(password);
//        HtmlPage myPage = submit.click();
//        while (myPage == null) {
//            Thread.sleep(1000);
//        }
//
//        LOGGER.info("####### 我的微博首页pc端 ####\n" + myPage.asXml());

    }


    /**
     * 下载图片到 D:\vcode.jpg
     * @param imageReader
     * @return
     * @throws IOException
     */
    public static boolean downloadImg(ImageReader imageReader) throws IOException {
        File file=new File("/vcodeimg.jpg");
        LOGGER.info("path:"+file.getAbsolutePath());
        if(!file.exists()){
            file.createNewFile();
        }
       return ImageIO.write(imageReader.read(0),"jpg",file);
    }

    /**
     * 搜索微博名
     *
     * @param name
     * @param client
     * @return 返回该用户的主页
     * @throws IOException
     */
    public HtmlPage search(String name, WebClient client) throws IOException, InterruptedException {
        String trueWeiboName = name;
        HtmlPage page = client.getPage("https://weibo.cn/search/?tf=5_012");
        page.getElementByName("keyword").setAttribute("value", name);
        HtmlPage suser = page.getElementByName("suser").click();
        //LOGGER.info("搜索结果:" + suser.asXml());
        DomElement weiboNameElement = suser.getFirstByXPath("/html/body/table[1]/tbody/tr/td[2]/a");
        if (weiboNameElement != null) {
            trueWeiboName = weiboNameElement.asText();
            LOGGER.info("查询出的微博名字为:" + trueWeiboName);
            HtmlPage userWeibo = weiboNameElement.click();
            while (userWeibo == null) {
                Thread.sleep(500);
            }
            return userWeibo;
        } else {
            LOGGER.info("搜索不到该微博号");
            throw new RuntimeException("搜索不到该微博号" + name);
        }
    }

    /**
     * 保存发布的一条微博到数据库
     *
     * @param page
     * @param id   wbuser表的id主键
     * @throws InterruptedException
     * @throws ParseException
     */
    public boolean saveWbToDB(HtmlPage page, Integer id, int pageNo) throws InterruptedException, ParseException {

        if (pageNo == 1) {
            DomText nameDomText = page.getFirstByXPath("/html/body/div[4]/table/tbody/tr/td[2]/div/span[1]/text()[1]");
            String name = nameDomText.getWholeText();
            DomText sex_address_dom = page.getFirstByXPath("/html/body/div[4]/table/tbody/tr/td[2]/div/span[1]/text()[2]");
            String sex_address = sex_address_dom.getWholeText();
            LOGGER.info("微博名" + name + " 性别/地址" + sex_address);
            HtmlElement commentSpan = page.getFirstByXPath("/html/body/div[4]/table/tbody/tr/td[2]/div/span[2]");
            String comment = commentSpan.asText();
            String weiboNum = page.querySelector("body > div.u > div > span").asText();
            weiboNum = weiboNum.substring(3, weiboNum.length() - 1);
            String guanZhuNum = page.querySelector("body > div.u > div > a:nth-child(2)").asText();
            guanZhuNum = guanZhuNum.substring(3, guanZhuNum.length() - 1);
            HtmlAnchor fansLink = page.querySelector("body > div.u > div > a:nth-child(3)");
            String fansNum = fansLink.asText();
            fansNum = fansNum.substring(3, fansNum.length() - 1);
            String href = fansLink.getHrefAttribute();
            String userId = href.substring(1, href.indexOf("/fans"));

            Wbuser oldUser = (Wbuser) wbuserService.selectById(id);
            oldUser.setNewName(name);
            oldUser.setComment(comment);
            oldUser.setWbNum(Integer.parseInt(weiboNum));
            oldUser.setGuanzhuNum(Integer.parseInt(guanZhuNum));
            oldUser.setFansNum(Integer.parseInt(fansNum));
            oldUser.setUserId(userId);
            wbuserService.updateById(oldUser);
        }

        boolean shouldGetNextPage = true;
        Date nowLine = new Date();
        int count = 0;
        DomNodeList<DomNode> weibos = page.querySelectorAll("div[id^='M_']");
        for (DomNode node : weibos) {
            count++;
            String postId = node.getAttributes().getNamedItem("id").getNodeValue();
            LOGGER.info("id" + postId);
            List<DomNode> divs = node.querySelectorAll("div");
            String finallyTime = "";
            String content = "无";
            String zan = "0";
            String zhuanfa = "0";
            String pinglun = "0";
            String url = "";
            String zhuanfaUrl = "";
            if (divs.size() == 1) {
                DomNode div = (DomElement) divs.get(0);
                DomNodeList<DomNode> spans = div.querySelectorAll("span");
                content = spans.get(0).asXml();
                String time = div.querySelector("span.ct").asText();
                finallyTime = time.substring(0, time.indexOf("来自"));
                DomNode zanNode = div.querySelector("a[href*='https://weibo.cn/attitude/']");
                if (zanNode != null) {
                    zan = zanNode.asText();
                    zan = zan.substring(2, zan.length() - 1);
                } else {
                    zan = div.querySelector("span.cmt").asText();
                    zan = zan.substring(3, zan.length() - 1);
                }
                zhuanfa = div.querySelector("a[href*='https://weibo.cn/repost/']").asText();
                zhuanfa = zhuanfa.substring(3, zhuanfa.length() - 1);
                zhuanfaUrl = div.querySelector("a[href*='https://weibo.cn/repost/']").getAttributes().getNamedItem("href").getNodeValue();
                pinglun = div.querySelector("a[href*='https://weibo.cn/comment/']").asText();
                pinglun = pinglun.substring(3, pinglun.length() - 1);
                url = div.querySelector("a[href*='https://weibo.cn/comment/']").getAttributes().getNamedItem("href").getNodeValue();
                LOGGER.info("id=" + postId + " time=" + time + " zan=" + zan + " zhuanfa=" + zhuanfa + " pinglun=" + pinglun + " url=" + url);
            } else if (divs.size() == 2) {

                DomNode div1 = divs.get(0);
                DomNode div2 = divs.get(1);

                content = div1.asXml();
                DomNode zanNode = div2.querySelector("a[href*='https://weibo.cn/attitude/']");
                if (zanNode != null) {
                    zan = zanNode.asText();
                    zan = zan.substring(2, zan.length() - 1);
                } else {
                    DomNodeList<DomNode> zanDiv = div2.querySelectorAll("span.cmt");
                    if (zanDiv.size() > 1) {
                        zan = zanDiv.get(1).asText();
                    } else {
                        zan = zanDiv.get(0).asText();
                    }
                    zan = zan.substring(3, zan.length() - 1);
                }
                zhuanfa = div2.querySelector("a[href*='https://weibo.cn/repost/']").asText();
                zhuanfa = zhuanfa.substring(3, zhuanfa.length() - 1);
                zhuanfaUrl = div2.querySelector("a[href*='https://weibo.cn/repost/']").getAttributes().getNamedItem("href").getNodeValue();

                pinglun = div2.querySelector("a[href*='https://weibo.cn/comment/']").asText();
                pinglun = pinglun.substring(3, pinglun.length() - 1);
                url = div2.querySelector("a[href*='https://weibo.cn/comment/']").getAttributes().getNamedItem("href").getNodeValue();
                String time = div2.querySelector("span.ct").asText();
                LOGGER.info("time" + time);
                finallyTime = time.substring(0, time.indexOf("来自"));
                LOGGER.info("id=" + id + " time=" + time + " zan=" + zan + " zhuanfa=" + zhuanfa + " pinglun=" + pinglun + " url=" + url);
            } else if (divs.size() == 3) {
                DomNode div1 = divs.get(0);
                DomNode div3 = divs.get(2);
                content = div1.asXml();

                DomText reason = div3.getFirstByXPath("//text()[1]");
                if (reason != null) {
                    String reasonStr = reason.getWholeText();
                    content = content + "      转发理由：" + reasonStr;
                }
                DomNode zanNode = div3.querySelector("a[href*='https://weibo.cn/attitude/']");
                if (zanNode != null) {
                    zan = zanNode.asText();
                    zan = zan.substring(2, zan.length() - 1);
                } else {
                    DomNodeList<DomNode> zanDiv = div3.querySelectorAll("span.cmt");
                    if (zanDiv.size() > 1) {
                        zan = zanDiv.get(1).asText();
                    } else {
                        zan = zanDiv.get(0).asText();
                    }
                }
                zhuanfa = div3.querySelector("a[href*='https://weibo.cn/repost/']").asText();
                zhuanfa = zhuanfa.substring(3, zhuanfa.length() - 1);
                zhuanfaUrl = div3.querySelector("a[href*='https://weibo.cn/repost/']").getAttributes().getNamedItem("href").getNodeValue();

                pinglun = div3.querySelector("a[href*='https://weibo.cn/comment/']").asText();
                pinglun = pinglun.substring(3, pinglun.length() - 1);
                url = div3.querySelector("a[href*='https://weibo.cn/comment/']").getAttributes().getNamedItem("href").getNodeValue();
                String time = div3.querySelector("span.ct").asText();
                System.out.println("time" + time);
                finallyTime = time.substring(0, time.indexOf("来自"));

            }
            finallyTime = converseTime(finallyTime.trim(), nowLine);
            //LOGGER.info("时间"+finallyTime);
            if (DateUtils.addDays(nowLine, -31).getTime() <= DateUtils.parseDate(finallyTime, pattern).getTime()) {
                //存入数据库
                Wbpost wbpost = new Wbpost();
                wbpost.setId(postId);
                wbpost.setContent(content);
                wbpost.setDianzanNum(Integer.parseInt(zan));
                wbpost.setPinglunNum(Integer.parseInt(pinglun));
                wbpost.setZhuanfaNum(Integer.parseInt(zhuanfa));
                wbpost.setTime(finallyTime);
                wbpost.setWbUserTableId(id);
                wbpost.setUrl(url);
                wbpost.setZhaunfaUrl(zhuanfaUrl);
                wbpostService.insertOrUpdate(wbpost);
                LOGGER.info(wbpost.toString());
            } else {
                if (count != 1 && pageNo != 1) {
                    shouldGetNextPage = false;  //时间不对不翻页了，并且不是置顶的
                    LOGGER.info("停止翻页");
                    LOGGER.info(finallyTime);
                    return shouldGetNextPage;
                }
            }
        }
        return shouldGetNextPage;
    }


    /**
     * 递归爬取一个完整的微博号内容
     *
     * @param indexPage
     * @param id        查找的微博名称
     * @param pageNo
     * @throws ParseException
     * @throws InterruptedException
     * @throws IOException
     */
    public void saveOneWholeWeibo(HtmlPage indexPage, Integer id, int pageNo) throws ParseException, InterruptedException, IOException {
        LOGGER.info("第" + pageNo + "页");

        boolean shouldGetNextPage = saveWbToDB(indexPage, id, pageNo);
        if (shouldGetNextPage) {
            HtmlAnchor nextPage = null;
            try {
                nextPage = indexPage.getAnchorByText("下页");
            } catch (ElementNotFoundException e) {
                return;
            }
            if (nextPage != null) {
                indexPage = nextPage.click();
                LOGGER.info("###########点击的了下页########");
                Thread.sleep(500);
                while (indexPage.querySelector(".b") == null) {
                    Thread.sleep(500);
                }
                pageNo = pageNo + 1;
                saveOneWholeWeibo(indexPage, id, pageNo);
            } else {
                //结束
                return;
            }
        } else {
            //结束
            return;
        }
    }

    /**
     * 爬取数据库中全部的微博号的 31 天内的文章
     * ps:仅仅爬取微博文章
     */
    public String saveAllWeiboTopic(WebClient client) throws IOException, InterruptedException, ParseException {
        StringBuilder sb = new StringBuilder();
        List<Wbuser> userList = wbuserService.selectList(null);

        for (int i = 0; i < userList.size(); i++) {
            Long s = System.currentTimeMillis();
            String name = userList.get(i).getWeiboname();
            HtmlPage userWeibo = search(name, client);
            Integer id = userList.get(i).getId();
            saveOneWholeWeibo(userWeibo, id, 1);
            Long e = System.currentTimeMillis();
            sb.append("微博号" + name + "爬取完成   耗时：" + (e - s) / 1000 + "秒\n");
        }
        return sb.toString();
    }

    /**
     * 爬取一篇微博的全部转发
     *
     * @param link
     * @param client
     * @param postId
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean saveWholeZhuanfaInOneWbToDB(String link, WebClient client, String postId) throws IOException, InterruptedException {
        HtmlPage page = client.getPage(link);
        HtmlSpan zfAnchor = page.querySelector("#rt");

        while (zfAnchor == null) {
            Thread.sleep(500);
        }
        String zfStr = zfAnchor.asText();
        if (zfStr.trim().length() == 2) {
            return true;  // 转发
        }
        zfStr = zfStr.substring(3, zfStr.length() - 1);
        if ("0".equals(zfStr)) {
            LOGGER.info("没有转发数,结束");
            return true;
        } else {
            while (true) {
                DomNodeList<DomNode> domNodes = page.querySelectorAll(".c");
                while (domNodes == null) {
                    Thread.sleep(500);
                    LOGGER.info("停了500毫秒");
                }

                Date nowLine = new Date();

                for (int i = 4; i < domNodes.getLength() - 1; i++) {
                    DomNode node = domNodes.get(i);
                    DomNodeList<DomNode> childNodes = node.getChildNodes();
                    HtmlElement user = (HtmlElement) childNodes.get(0);
                    int k = 1;
                    if (user instanceof HtmlSpan) {
                        LOGGER.info("第一个是span类型");  //不去读取热门 ，因为后面可以获取到
                        continue;
                    }
                    String userIdHref = user.getAttribute("href");
                    String userId="";  //  "/u/123456"或者/修改了域名的名字没有 /u
                    if (userIdHref.indexOf("/u/")!=-1){
                        userId=userIdHref.substring(3);
                    }else {
                        userId=userIdHref.substring(1);
                    }
                    String userName = user.getTextContent();
                    String content = "";

                    for (int j = k; j < childNodes.size() - 2; j++) {
                        content = content + childNodes.get(j).asXml();
                    }
                    String ct = childNodes.get(childNodes.getLength() - 1).asText().trim();
                    ct = ct.substring(0, ct.indexOf("来自"));
                    String finallyTime = converseTime(ct, nowLine);
                    Wbzhuanfa wbzhuanfa = new Wbzhuanfa();
                    wbzhuanfa.setId(String.valueOf(IdWorker.getId()));
                    wbzhuanfa.setFromName(userName);
                    wbzhuanfa.setFromUid(userId);
                    wbzhuanfa.setContent(content);
                    wbzhuanfa.setPosturl(link);
                    wbzhuanfa.setPostid(postId);
                    wbzhuanfa.setTime(finallyTime);
                    wbzhuanfa.insert();
                }
                HtmlAnchor nextPageLink = null;
                try {
                    nextPageLink = page.getAnchorByText("下页");
                    LOGGER.info("点击下页");
                    page = nextPageLink.click();

                } catch (ElementNotFoundException e) {
                    return true;
                }

            }//
        }
    }


    /**
     * 爬取全部的微博转发
     *
     * @param client
     */
    public String saveAllWeiboZhuanfa(WebClient client) throws IOException, InterruptedException {
        LOGGER.info("####### 启动转发爬取 ########## ");
        StringBuilder sb = new StringBuilder();
        List<Wbuser> levelOne = wbuserService.getZhuanfaUserList();
        Map map=new HashMap(1);
        for (Wbuser wbuser : levelOne) {
            LOGGER.info("开始《" + wbuser.getWeiboname() + "》 真实微博名+" + wbuser.getNewName() + "的转发爬取");
            map.put("WBUSERTABLE_ID", wbuser.getId());
            List<Wbpost> user_Post_List = wbpostService.selectByMap(map);
            for (Wbpost wbpost : user_Post_List) {
                LOGGER.info("微博链接" + wbpost.getZhaunfaUrl());
                Long s = System.currentTimeMillis();
                saveWholeZhuanfaInOneWbToDB(wbpost.getZhaunfaUrl(), client, wbpost.getId());
                Long e = System.currentTimeMillis();
                sb.append("爬取转发微博" + wbpost.getUrl() + " 耗时" + (e - s) / 1000 + "秒\n");
            }
        }
        LOGGER.info("#############  转发爬取结束 #########   ");
        return sb.toString();
    }

    /**
     * 爬取一个文章下的全部评论
     * @param link
     * @param client
     * @param postId
     * @throws IOException
     * @throws InterruptedException
     */
    public void saveWholeCommentInOneWbToDB(String link,WebClient client,String postId) throws IOException, InterruptedException {
        HtmlPage page=client.getPage(link);
        HtmlSpan zanSpan = page.querySelector("span.pms");
        String zanNum=zanSpan.asText().trim();
        if(zanNum.length()==2||zanNum.substring(3,zanNum.length()-1).equals("0")){
            LOGGER.info("===没有评论==="+link);
            return;
        }else {
            while (true) {
                DomNodeList<DomNode> domNodes = page.querySelectorAll(".c");
                while (domNodes == null) {
                    Thread.sleep(500);
                    LOGGER.info("停了500毫秒");
                }

                Date nowLine = new Date();

                for (int i = 4; i < domNodes.getLength() - 1; i++) {

                    DomNode node = domNodes.get(i);
                    DomNodeList<DomNode> childNodes = node.getChildNodes();

                    DomNode firstEle = childNodes.get(0);
                    //LOGGER.info(firstEle.getNodeName()+"-"+firstEle.getNodeValue());
                    if (firstEle instanceof HtmlSpan || childNodes.getLength()<5) {
                        LOGGER.info("热门或者查看更多"); //直接不读取热门，因为后面可以抓取得到
                        continue;
                    }
                    HtmlAnchor user=node.querySelector("a");
                    String userIdHref = user.getAttribute("href");
                    String userId="";  //  "/u/123456"或者/修改了域名的名字没有 /u/
                    if (userIdHref.indexOf("/u/")!=-1){
                        userId=userIdHref.substring(3);
                    }else {
                        userId=userIdHref.substring(1);
                    }

                    String userName = user.getTextContent();
                    String content = node.querySelector(".ctt").asXml();
                    String ct = childNodes.get(childNodes.getLength() - 1).asText().trim();
                    ct = ct.substring(0, ct.indexOf("来自"));
                    String finallyTime = converseTime(ct, nowLine);
                    Wbcomment wbcomment = new Wbcomment();
                    wbcomment.setId(String.valueOf(IdWorker.getId()));
                    wbcomment.setFromName(userName);
                    wbcomment.setFromUid(userId);
                    wbcomment.setContent(content);
                    wbcomment.setPosturl(link);
                    wbcomment.setPostid(postId);
                    wbcomment.setTime(finallyTime);
                    wbcomment.insert();
                }
                HtmlAnchor nextPageLink = null;
                try {
                    nextPageLink = page.getAnchorByText("下页");
                    LOGGER.info("点击下页");
                    page = nextPageLink.click();
                } catch (ElementNotFoundException e) {
                    return ;
                }

            }
        }
    }

    /**
     * 爬取全部微博评论
     * @param client
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String saveAllWeiboComment(WebClient client) throws IOException, InterruptedException {
        LOGGER.info("####### 启动评论爬取 ########## ");
        StringBuilder sb = new StringBuilder();
        Map map = new HashMap<String, Object>(1);
        map.put("CRAWLEVEL", 2);
        List<Wbuser> levelOne = wbuserService.selectByMap(map);
        for (Wbuser wbuser : levelOne) {
            LOGGER.info("开始《" + wbuser.getWeiboname() + "》 真实微博名+" + wbuser.getNewName() + "的评论爬取");
            map.clear();
            map.put("WBUSERTABLE_ID", wbuser.getId());
            List<Wbpost> user_Post_List = wbpostService.selectByMap(map);
            for (Wbpost wbpost : user_Post_List) {
                LOGGER.info("微博链接" + wbpost.getUrl());
                Long s = System.currentTimeMillis();
                saveWholeCommentInOneWbToDB(wbpost.getUrl(), client, wbpost.getId());
                Long e = System.currentTimeMillis();
                sb.append("爬取评论微博" + wbpost.getUrl() + " 耗时" + (e - s) / 1000 + "秒\n");
            }
        }
        LOGGER.info("#############  评论爬取结束 #########   ");
        return sb.toString();
    }






    /**
     * 将时间转化成yyyy-MM-dd HH:mm:ss
     *
     * @param str
     * @param nowLine
     * @return
     */
    private String converseTime(String str, Date nowLine) {
        Calendar calendar = Calendar.getInstance();
        String time = "";
        String nowYear = String.valueOf(calendar.get(Calendar.YEAR));
        String nowMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String nowDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (nowMonth.length() == 1) {
            nowMonth = "0" + nowMonth;
        }
        if (nowDay.length() == 1) {
            nowDay = "0" + nowDay;
        }
        //final Date limitDate=DateUtils.addDays(nowLine,-31);
        if (str.indexOf("月") != -1 && str.indexOf("日") != -1) {
            String month = str.substring(0, 2);
            String day = str.substring(3, 5);
            String hour = str.substring(7, 9);
            String min = str.substring(10, 12);
            time = nowYear + "-" + month + "-" + day + " " + hour + ":" + min + ":" + "00";

        } else if (str.indexOf("前") != -1 && str.indexOf("分") != -1) {
            String num = str.substring(0, str.indexOf("分"));
            Date date = DateUtils.addMinutes(nowLine, Integer.parseInt(num));
            time = DateFormatUtils.format(date, pattern[0]);
        } else if (str.indexOf("今天") != -1) {
            String suf = str.substring(str.indexOf("天") + 1).trim();
            time = nowYear + "-" + nowMonth + "-" + nowDay + " " + suf + ":00";
        } else {
            time = str;
        }
        return time;
    }


    public void test2() throws IOException, InterruptedException {
        WebClient client = getClient();
        Page page = client.getPage("https://m.weibo.cn/p/100103type%3D1%26q%3D%E7%8E%8B%E5%AE%9D%E5%BC%BA?type=all&queryVal=%E7%8E%8B%E5%AE%9D%E5%BC%BA&featurecode=20000320&luicode=10000011&lfid=100103type%3D1%26q%3Dsss&title=%E7%8E%8B%E5%AE%9D%E5%BC%BA");
        Thread.sleep(10000);
        LOGGER.info(page.getWebResponse().getResponseHeaders().toString());
        LOGGER.info("直接getPage" + page.toString() + "url\n" + page.getUrl() + "是html类型?" + page.isHtmlPage() + "response\n" + page.getWebResponse().getContentAsString());

        client.close();
    }


}
