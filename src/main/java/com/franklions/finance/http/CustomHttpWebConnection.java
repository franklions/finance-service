package com.franklions.finance.http;

import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-11-28
 * @since Jdk 1.8
 */
public class CustomHttpWebConnection extends HttpWebConnection {
    /**
     * Creates a new HTTP web connection instance.
     *
     * @param webClient the WebClient that is using this connection
     */
    public CustomHttpWebConnection(WebClient webClient) {
        super(webClient);
    }

    @Override
    protected HttpClientBuilder getHttpClientBuilder() {
        HttpClientBuilder builder =  super.getHttpClientBuilder();
        //关闭重试功能
        builder.disableAutomaticRetries();
        return  builder;
    }
}
