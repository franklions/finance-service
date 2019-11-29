package com.franklions.finance.service.downloader;

import com.franklions.finance.http.CustomHtmlUnitDriver;
import com.franklions.finance.service.RequestUseTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-20
 * @since Jdk 1.8
 */
public class HtmlUnitDownloader implements Downloader, Closeable {

    private static final ThreadLocal<CustomHtmlUnitDriver> driverPool = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(HtmlUnitDownloader.class);
//    final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20160101 Firefox/66.0";

    private int poolSize =1;

    @Override
    public Page download(Request request, Task task) {

        CustomHtmlUnitDriver driver=driverPool.get();
        if(driver == null ){
            driver = new CustomHtmlUnitDriver();
            driver.modifyWebClient();
            driverPool.set(driver);
        }

        RequestUseTime.threadStartTime.set(System.currentTimeMillis());

        Page page = new Page();
        String content="";
        try {
            driver.get(request.getUrl());

            WebElement element = new WebDriverWait(driver,2).until(
                    ExpectedConditions.presenceOfElementLocated(By.className("pj_bar")));
            content = driver.getPageSource();
        } catch (Exception e) {
            logger.error("获取页页元素超时异常:"+request.getUrl(),e);
            content = driver.getPageSource();
        }finally {

        }

        page.setRawText(content);
        page.setHtml(new Html(content, request.getUrl()));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        return page;
    }

    @Override
    public void setThread(int threadNum) {
        this.poolSize = threadNum;
    }

    @Override
    public void close() throws IOException {
//        CustomHtmlUnitDriver driver=driverPool.get();
//        if(driver != null ){
//            if(!driver.isClosed()){
//                driver.close();
//            }
//            driverPool.remove();
//        }
    }
}
