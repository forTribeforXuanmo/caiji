package com.caiji.weibo;

import com.caiji.weibo.entity.Wbpost;
import com.caiji.weibo.service.IWbcommentService;
import com.caiji.weibo.service.IWbpostService;
import com.caiji.weibo.service.IWbuserService;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.NamedNodeMap;

import javax.sound.midi.Soundbank;
import javax.swing.text.html.HTML;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-9-4.
 */
@Component
public class Handler {

    @Autowired
    private IWbuserService wbuserService;
    @Autowired
    private IWbpostService wbpostService;
    @Autowired
    private IWbcommentService wbcommentService;

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    // private static String currentYear = null;
    private final String pattern[] = {"yyyy-MM-dd HH:mm:ss"};

    public WebClient getClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        //  webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(100000);
        return webClient;
    }

    public static void main(String[] args) throws Exception {
        long s = System.currentTimeMillis() / 1000;
        Handler handler = new Handler();
        WebClient client = handler.getClient();
        handler.login(client);
        String name="轩墨宝宝QAQ";
        HtmlPage userWeibo = handler.search(name, client);
        logger.info("微博首页第一页" + userWeibo.asXml());
        handler.readOneWholeWeibo(userWeibo,name,1);
        long e = System.currentTimeMillis() / 1000;
        logger.info("一共花了  ：" + (e - s));
    }
    public void readOneWholeWeibo(HtmlPage indexPage,String name,int pageNo) throws ParseException, InterruptedException, IOException {
        logger.info("第"+pageNo+"页");
        boolean shouldGetNextPage =weiboToDB(indexPage, name,pageNo);
        if(shouldGetNextPage){
            HtmlAnchor nextPage = indexPage.getAnchorByText("下页");
            if(nextPage!=null){
                indexPage = nextPage.click();
                Thread.sleep(1000);
                readOneWholeWeibo(indexPage,name,pageNo++);
            }else {
                //结束
                return;
            }
        }else {
            //结束
            return;
        }
    }






    /**
     * 登录
     * @param client
     * @return
     * @throws IOException
     */
    public HtmlPage login(WebClient client) throws IOException {
        HtmlPage page = client.getPage("https://passport.weibo.cn/signin/login?entry=mweibo&r=http%3A%2F%2Fweibo.cn%2F%3Ffeaturecode%3D20000320%26luicode%3D10000011%26lfid%3D100103type%253D1%2526q%253DEMINEM%25E4%25B8%25AD%25E6%2596%2587%25E7%25BD%2591%25E5%25AE%2598%25E6%2596%25B9%25E5%25BE%25AE%25E5%258D%259A&backTitle=%CE%A2%B2%A9&vt=");
        HtmlInput userName = (HtmlInput) page.getElementById("loginName");
        HtmlInput pwd = (HtmlInput) page.getElementById("loginPassword");
        HtmlAnchor submit = (HtmlAnchor) page.getElementById("loginAction");
        userName.setValueAttribute("15896264186");
        pwd.setValueAttribute("lishengzhu");
        HtmlPage myPage = submit.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("####### 我的微博首页 ####\n" + myPage.asXml());
        return myPage;
    }


    public HtmlPage search(String name, WebClient client) throws IOException {
        String trueWeiboName = name;
        HtmlPage page = client.getPage("https://weibo.cn/search/?tf=5_012");
        page.getElementByName("keyword").setAttribute("value", name);
        HtmlPage suser = page.getElementByName("suser").click();
        logger.info("搜索结果:" + suser.asXml());
        DomElement weiboNameElement = suser.getFirstByXPath("/html/body/table[1]/tbody/tr/td[2]/a");
        if (weiboNameElement != null) {
            trueWeiboName = weiboNameElement.asText();
            logger.info("查询出的微博名字为:" + trueWeiboName);
            HtmlPage userWeibo = weiboNameElement.click();
            return userWeibo;
        } else {
            logger.info("搜索不到该微博号");
            throw new RuntimeException("搜索不到该微博号" + name);
        }
    }

    /**
     *
     * @param page
     * @param weiboName
     * @throws InterruptedException
     * @throws ParseException
     */
    public boolean weiboToDB(HtmlPage page,String weiboName,int pageNo) throws InterruptedException, ParseException {
        boolean shouldGetNextPage=true;
        Date nowLine = new Date();
        int count=0;
        DomNodeList<DomNode> weibos = page.querySelectorAll("div[id^='M_']");
        for (DomNode node:weibos) {
            count++;
            String id = node.getAttributes().getNamedItem("id").getNodeValue();
            logger.info("id" + id);
            List<DomNode> divs=node.querySelectorAll("div");
            String finallyTime = "";
            String content="无";
            String zan="0";
            String zhuanfa="0";
            String pinglun="0";
            String url="";
            if (divs.size() == 1) {
                DomNode div = (DomElement) divs.get(0);
                DomNodeList<DomNode> spans = div.querySelectorAll("span");
                 content = spans.get(0).asXml();
                String time = spans.get(1).asText();
                logger.info("时间span打印" + time);
                finallyTime = time.substring(0, time.indexOf("来自"));
                zan = div.querySelector("a[href*='https://weibo.cn/attitude/']").asText();
                zan = zan.substring(2, zan.length() - 1);
                zhuanfa = div.querySelector("a[href*='https://weibo.cn/repost/']").asText();
                zhuanfa = zhuanfa.substring(3, zhuanfa.length() - 1);
                pinglun = div.querySelector("a[href*='https://weibo.cn/comment/']").asText();
                pinglun = pinglun.substring(3, pinglun.length() - 1);
                url = div.querySelector("a[href*='https://weibo.cn/comment/']").getAttributes().getNamedItem("href").getNodeValue();
                logger.info("id=" + id + "time" + time + "zan" + zan + "zhuanfa" + zhuanfa + "pinglun" + pinglun + " url" + url);
            } else  {

                DomNode div1 = divs.get(0);
                DomNode div2 = divs.get(1);
                if(divs.size()==3){
                    div2=divs.get(2);
                }
                content = div1.asXml();
                zan = div2.querySelector("a[href*='https://weibo.cn/attitude/']").asText();
                zan = zan.substring(2, zan.length() - 1);
                zhuanfa = div2.querySelector("a[href*='https://weibo.cn/repost/']").asText();
                zhuanfa = zhuanfa.substring(3, zhuanfa.length() - 1);
                pinglun = div2.querySelector("a[href*='https://weibo.cn/comment/']").asText();
                pinglun = pinglun.substring(3, pinglun.length() - 1);
                url = div2.querySelector("a[href*='https://weibo.cn/comment/']").getAttributes().getNamedItem("href").getNodeValue();
                String time = div2.querySelector("span").asText();
                if(divs.size()==3){
                    time=div2.querySelectorAll("span").get(1).asText();
                }
                System.out.println("time"+time);
                finallyTime = time.substring(0, time.indexOf("来自"));
                logger.info("id=" + id + "time" + time + "zan" + zan + "zhuanfa" + zhuanfa + "pinglun" + pinglun + " url" + url);
            }
            finallyTime=converseTime(finallyTime,nowLine);
            //logger.info("时间"+finallyTime);
            if(DateUtils.addDays(nowLine,-31).getTime()<=DateUtils.parseDate(finallyTime,pattern).getTime()){
                //存入数据库
                Wbpost wbpost=new Wbpost();
                wbpost.setId(id);
                wbpost.setContent(content);
                wbpost.setDianzanNum(Integer.parseInt(zan));
                wbpost.setPinglunNum(Integer.parseInt(pinglun));
                wbpost.setZhuanfaNum(Integer.parseInt(zhuanfa));
                wbpost.setTime(finallyTime);
                wbpost.setWeiboname(weiboName);
                wbpost.setUrl(url);
               // wbpost.insert();
                logger.info(wbpost.toString());
            }else {
                if(count!=1&&pageNo!=1){
                    shouldGetNextPage=false;  //时间不对不翻页了，并且不是置顶的
                }
            }
        }
        return shouldGetNextPage;
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
        final String nowYear = String.valueOf(calendar.get(Calendar.YEAR));
        final String nowMonth = String.valueOf(calendar.get(Calendar.MONTH));
        final String nowDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
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
        logger.info(page.getWebResponse().getResponseHeaders().toString());
        logger.info("直接getPage" + page.toString() + "url\n" + page.getUrl() + "是html类型?" + page.isHtmlPage() + "response\n" + page.getWebResponse().getContentAsString());

        client.close();
    }


    public void submittingForm() throws Exception {
        final WebClient webClient = new WebClient();

        // Get the first page
        final HtmlPage page1 = webClient.getPage("http://www.baidu.com/");

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
        final HtmlForm form = page1.getFormByName("f");

        final HtmlSubmitInput button = form.getInputByValue("百度一下");
        final HtmlTextInput textField = form.getInputByName("wd");

        // Change the value of the text field
        textField.setValueAttribute("htmlUnit");

        // Now submit the form by clicking the button and get back the second
        // page.
        final HtmlPage page2 = button.click();
        for (int i = 0; i < 20; i++) {
            if (!page2.getByXPath("//div[@class='c-gap-top c-recommend']").isEmpty()) {
                break;
            }
            synchronized (page2) {
                page2.wait(500);
            }
        }
        FileWriter writer = new FileWriter("baidu.html");
        writer.write(page2.asXml());
        writer.close();
        // System.out.println(page2.asXml());

        webClient.close();
    }


}
