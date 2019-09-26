package com.franklions.finance.service.downloader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
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

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-20
 * @since Jdk 1.8
 */
public class HtmlUnitDownloader implements Downloader {

    private static final Logger logger = LoggerFactory.getLogger(HtmlUnitDownloader.class);

    private int poolSize =1;

    @Override
    public Page download(Request request, Task task) {
        HtmlUnitDriver dirver= new HtmlUnitDriver();
        Page page = new Page();
        String content="";
        try {
            //设置js脚本
            dirver.setJavascriptEnabled(true);
            dirver.get(request.getUrl());

//            WebElement element= (new WebDriverWait(dirver, 1 )).until(
//                    new ExpectedCondition<WebElement>(){
//                        @Override
//                        public  WebElement apply( WebDriver d) {
//                            return  d.findElement( By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/div/div[3]/div[1]/div[2]" ));
//                        }
//                    });
            WebElement element = new WebDriverWait(dirver,2).until(
                    ExpectedConditions.presenceOfElementLocated(By.className("pj_bar")));
            content = dirver.getPageSource();
        } catch (Exception e) {
            logger.error("获取页页元素超时异常:",e);
        }finally {
            try {
                dirver.close();
            }catch (Exception e){

            }
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
}
