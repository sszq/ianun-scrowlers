package com.ianun.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description: The crawlers utils
 * @date ......: 2020-December-Wednesday
 */
public class HttpUtils {

    @Autowired
    private static Environment environment;

    @Value("${crawlers.path}")
    private static String path;

    public static PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();

    public HttpUtils() {
        // Set max connection count
        manager.setMaxTotal(100);
        // Set max connection count in every host
        manager.setDefaultMaxPerRoute(10);
    }

    /**
     * 1.getHtml
     * 2.downloadImg
     */

    private static RequestConfig getConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                // The max long time of Creating connection
                .setConnectTimeout(10 * 1000)
                // The max long time of Getting connection
                .setConnectionRequestTimeout(10 * 1000)
                // The max long time of data transfer
                .setSocketTimeout(10 * 1000)
                .build();
        return requestConfig;
    }

    /**
     * getHtmlBody
     * @param url
     * @return
     */
    public static String getHtmlContent(String url) {
        String htmlString = null;
        // Create HttpClient instance
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager).build();
        // Create HttpGet request
        HttpGet httpGet = new HttpGet(url);
        // Set HttpGet request details
        httpGet.setConfig(getConfig());

        // Send HttpClient request, and get the http Response
        try( CloseableHttpResponse httpResponse = httpClient.execute(httpGet);) {

            if (null != httpClient && 200 == httpResponse.getStatusLine().getStatusCode()) {
                htmlString = EntityUtils.toString(httpResponse.getEntity(), "utf8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlString;
    }

    public static void downloadImage(String url) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager).build();
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getConfig());
        try {
            final CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (null != httpResponse && 200 == httpResponse.getStatusLine().getStatusCode()) {
                File imgFile = new File("");
                OutputStream outputStream = new FileOutputStream(imgFile);
                httpResponse.getEntity().writeTo(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileName(String url) {
        String fileName = null;
        Pattern pattern = Pattern.compile("(?<=/)[^/]*(?=$)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            fileName = matcher.group(0);
        }

        return fileName;
    }

    public static void main(String[] args) {
        String url = "https://www.assdrty.com/images/2020/11/30/015391a363732d5696.jpg";
        System.out.println(getFileName(url));
        System.out.println(path);
    }
}
