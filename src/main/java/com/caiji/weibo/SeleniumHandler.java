/*
package com.caiji.weibo;

import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.awt.windows.WEmbeddedFrame;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

*/
/**
 * Created by Administrator on 2017-9-6.
 *//*

public class SeleniumHandler {

    private static String currentYear = null;
    private static Date limitDate = null;
    private final static String[] parsePatterns = {"yyyy-MM-dd"};
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumHandler.class);


    public static void main(String[] args) throws InterruptedException, ParseException {

        WebDriver driver = getDriver();
        login(driver, "15896264186", "lishengzhu");
        searchRedirct("林允儿",driver);
        getWeibo(driver);
    }

    */
/**
     * 获取微博
     *
     * @param driver
     * @return 返回微博文章集合
     * @throws ParseException
     * @throws InterruptedException
     *//*

    public static List<WebElement> getWeibo(WebDriver driver) throws ParseException, InterruptedException {
        List<WebElement> elements = driver.findElements(By.cssSelector("div[id^='M_']"));
        int  size=elements.size();
        List<WebElement> divs = elements.get(size - 1).findElements(By.tagName("div"));
        String id=elements.get(size - 1).getAttribute("id");
        if(divs.size()==1){
            WebElement div=divs.get(0);
            List<WebElement> spans=div.findElements(By.tagName("span"));
            String content=spans.get(0).getAttribute("innerHTML");
            String time=spans.get(1).getText();
            LOGGER.info("时间span打印"+time);
            time=time.substring(0,time.indexOf("来自"));
            String zan = div.findElement(By.cssSelector("a[href*='/attitude/']")).getText();
            zan=zan.substring(2,zan.length()-2);
            String zhuanfa = div.findElement(By.cssSelector("a[href*='/repost/']")).getText();
            zhuanfa=zhuanfa.substring(3,zhuanfa.length()-2);
            String pinglun = div.findElement(By.cssSelector("a[href*='/comment/']")).getText();
            pinglun=pinglun.substring(3,pinglun.length()-2);
            String url=div.findElement(By.cssSelector("a[href*='/comment/']")).getAttribute("href");
            LOGGER.info("id="+id+"time"+time+"zan"+zan+"zhuanfa"+zhuanfa+"pinglun"+pinglun+" url"+url);
        }else if(divs.size()==2){
            WebElement div1=divs.get(0);
            WebElement div2=divs.get(1);
            String content=div1.getAttribute("innerHTML");
            String zan = div2.findElement(By.cssSelector("a[href*='/attitude/']")).getText();
            zan=zan.substring(2,zan.length()-2);
            String zhuanfa = div2.findElement(By.cssSelector("a[href*='/repost/']")).getText();
            zhuanfa=zhuanfa.substring(3,zhuanfa.length()-2);
            String pinglun = div2.findElement(By.cssSelector("a[href*='/comment/']")).getText();
            pinglun=pinglun.substring(3,pinglun.length()-2);
            String url=div2.findElement(By.cssSelector("a[href*='/comment/']")).getAttribute("href");
            String time=div2.findElement(By.tagName("span")).getText();
            time=time.substring(0,time.indexOf("来自"));
            LOGGER.info("id="+id+"time"+time+"zan"+zan+"zhuanfa"+zhuanfa+"pinglun"+pinglun+" url"+url);
        }
        return elements;

    }


    */
/**
     * 点击评论标签进入 获取评论
     *
     * @param driver
     * @param member 一个微博文章的WebElement
     * @return
     * @throws InterruptedException
     *//*

    public static List<WebElement> getComment(WebDriver driver, WebElement member) throws InterruptedException, ParseException {
        String time = member.findElement(By.className("time")).getText();
        List<WebElement> commentList = null;
        if (time.length() == 5 && time.indexOf("-") != -1) {
            time = currentYear + "-" + time;
        }
        Date date = null;
        if (time.length() == 10 && time.indexOf("-") != -1) {
            date = DateUtils.parseDate(time, parsePatterns);
        }
        LOGGER.info("member的xml" + member.getAttribute("innerHTML"));
        if ((date != null && date.getTime() > limitDate.getTime()) || time.indexOf("-") == -1) {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", member);

            member.findElement(By.tagName("footer")).findElements(By.tagName("div")).get(1).click();
            Thread.sleep(300);

            int total = 0;
            boolean isEnd = false;
            int times = 0;
            while (isEnd == false) {
                int oldTotal = total;
                for (int i = 0; i < 5; i++) {
                    commentList = driver.findElements(By.className("comment-item"));
                    if (commentList != null) {
                        total = commentList.size();
                    }
                    if (oldTotal != total) {
                        break;
                    } else {
                        Thread.sleep(500);
                    }
                }
                scrollToFoot(driver);
                if (oldTotal == total) {
                    isEnd = true;
                }
            }
        }
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("当前url" + currentUrl);
        return commentList;
    }

    public static void readComment(List<WebElement> commentList, Long postId) {
        for (WebElement element : commentList) {
            String useName = element.findElement(By.className("comment-user-name")).getText();
            String time = element.findElement(By.className("comment-time")).getText();
            String dz = element.findElement(By.className("comment-dz-num")).getText();
            String content = element.findElement(By.className("comment-con")).getText();
            LOGGER.info("userName:" + useName + " 时间：" + time + " 评论：" + content + " 点赞数：" + dz);
        }
    }


    public static void getWeiboWithZhuanfa(String weiboName, WebDriver driver) throws InterruptedException, ParseException {
        //先读取需要的评论
        List<WebElement> weiboList = getWeibo(driver);//这个集合中有可能包含31天以外的
        readWeibo(weiboList, weiboName);   //读取微博，保存数据库
        String windowHandle = driver.getWindowHandle();
        for (WebElement element : weiboList) {
            List<WebElement> commentList = getComment(driver, element);
            readComment(commentList, 1254l);
            driver.switchTo().window(windowHandle);
        }

    }


    */
/**
     * 获取WebDriver
     *
     * @return
     *//*

    public static WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    */
/**
     * JS控制滚动条的位置：
     * <p>
     * window.scrollTo(x,y);
     * <p>
     * 竖向滚动条置顶 window.
     * <p>
     * scrollTo(0,0);
     * <p>
     * 竖向滚动条置底 window.
     * <p>
     * scrollTo(0,document.body.scrollHeight)
     * <p>
     * JS控制TextArea滚动条自动滚动到最下部
     * document.getElementById('textarea').scrollTop =document.getElementById('textarea').scrollHeight
     * 窗口滚动条滑倒底部
     *
     * @param driver
     *//*

    public static void scrollToFoot(WebDriver driver) {
        JavascriptExecutor driver_js = (JavascriptExecutor) driver;
        driver_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }


    */
/**
     * 操作 读取微博集合
     *
     * @param weiboList
     * @param weiboName 微博名
     *//*

    public static void readWeibo(List<WebElement> weiboList, String weiboName) throws ParseException {
        int number = 0;
        for (WebElement element : weiboList) {
            String time = element.findElement(By.className("time")).getText();
            if (time.length() == 5 && time.indexOf("-") != -1) {
                time = currentYear + "-" + time;
            }
            Date date = null;
            if (time.length() == 10 && time.indexOf("-") != -1) {
                date = DateUtils.parseDate(time, parsePatterns);
            }
            if ((date != null && date.getTime() > limitDate.getTime()) || time.indexOf("-") == -1) {
                String from = element.findElement(By.className("from")).getText();
                String content = element.findElement(By.className("weibo-main")).getAttribute("innerHTML");
                List<WebElement> footer = element.findElement(By.tagName("footer")).findElements(By.tagName("div"));
                String zhuanfa = footer.get(0).getText();
                String pinglun = footer.get(1).getText();
                String dianzan = footer.get(2).getText();
                LOGGER.info("微博用户：" + weiboName);
                LOGGER.info("时间：" + time + "  " + from);
                LOGGER.info("微博内容：" + content);
                LOGGER.info("转发" + zhuanfa + "-评论" + pinglun + "-点赞" + dianzan);
                number++;
            }
        }
        LOGGER.info("一共有" + number);
    }


    */
/**
     * 登录微博
     *
     * @param driver
     * @return
     *//*

    public static WebDriver login(WebDriver driver, String userName, String password) {
        driver.get("https://passport.weibo.cn/signin/login?entry=mweibo&r=http%3A%2F%2Fweibo.cn%2F%3Ffeaturecode%3D20000320%26luicode%3D10000011%26lfid%3D100103type%253D1%2526q%253DEMINEM%25E4%25B8%25AD%25E6%2596%2587%25E7%25BD%2591%25E5%25AE%2598%25E6%2596%25B9%25E5%25BE%25AE%25E5%258D%259A&backTitle=%CE%A2%B2%A9&vt=");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement login = driver.findElement(By.xpath("/*/
/*[@id=\"loginName\"]"));
        login.click();
        login.sendKeys(userName);
        driver.findElement(By.xpath("/*/
/*[@id=\"loginPassword\"]")).sendKeys(password);
        driver.findElement(By.xpath("/*/
/*[@id=\"loginAction\"]")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return driver;
    }

    */
/**
     * 首页中点击搜索
     *
     * @param driver
     * @throws InterruptedException
     *//*

    public static void clickSearch(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement e = (new WebDriverWait(driver, 10)).until(
                new ExpectedCondition<WebElement>() {
                    @Override
                    public WebElement apply(WebDriver d) {
                        return d.findElement(By.cssSelector("a[title='搜索']"));
                    }
                });
        e.click();
        Thread.sleep(2000);
    }


    public static String searchRedirct(String weiboName, WebDriver driver) throws InterruptedException {
        driver.get("https://weibo.cn/search/?tf=5_012");
        return search(weiboName, driver);
    }


    */
/**
     * 查找微博号，并进入该微博号主页
     *
     * @param weiboName 微博名字
     * @param driver
     * @return 查找的真实微博名
     * @throws InterruptedException
     *//*

    public static String search(String weiboName, WebDriver driver) throws InterruptedException {
        String trueWeiboName = weiboName;
        WebElement queryVal = driver.findElement(By.name("keyword"));
        queryVal.click();
        queryVal.sendKeys(weiboName);
        driver.findElement(By.name("suser")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement weiboNameElement = driver.findElement(By.xpath("/html/body/table[1]/tbody/tr/td[2]/a"));
        if (weiboNameElement != null) {
            trueWeiboName = weiboNameElement.getText();
            LOGGER.info("查询出的微博名字为:" + trueWeiboName);
            weiboNameElement.click();
            Thread.sleep(2000);
        } else {
            LOGGER.info("搜索不到该微博号");
            throw new RuntimeException("搜索不到该微博号" + weiboName);
        }
        return trueWeiboName;
    }

}
*/
