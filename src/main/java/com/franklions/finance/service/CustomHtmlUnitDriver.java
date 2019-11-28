package com.franklions.finance.service;

import com.franklions.finance.http.CustomHttpWebConnection;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-29
 * @since Jdk 1.8
 */
public class CustomHtmlUnitDriver extends HtmlUnitDriver {
    public CustomHtmlUnitDriver(BrowserVersion browser) {
        super(browser);
    }

    public CustomHtmlUnitDriver(){
        super();
    }

    public void modifyWebClient() {
        WebClient newWebClient = getWebClient();
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        newWebClient.getOptions().setThrowExceptionOnScriptError(false);
        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        newWebClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        newWebClient.getOptions().setPrintContentOnFailingStatusCode(false);
        newWebClient.getOptions().setActiveXNative(false);
        newWebClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        newWebClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        newWebClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

//        webClient.getOptions().setTimeout(timeout);//设置“浏览器”的请求超时时间
//        webClient.setJavaScriptTimeout(timeout);//设置JS执行的超时时间

        modifyWebClient(newWebClient);
    }

    @Override
    protected WebClient newWebClient(BrowserVersion version) {
        WebClient client = super.newWebClient(version);
        client.setWebConnection(new CustomHttpWebConnection(client));
        return  client;
    }

    public Boolean isClosed(){
        try {
            getCurrentWindow();
        }catch (NoSuchWindowException | NoSuchSessionException e){
            return true;
        }

        return false;
    }
}
