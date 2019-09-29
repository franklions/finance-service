package com.franklions.finance.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
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
        newWebClient.getOptions().setThrowExceptionOnScriptError(false);
        newWebClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        newWebClient.getOptions().setPrintContentOnFailingStatusCode(false);
        modifyWebClient(newWebClient);
    }
}
