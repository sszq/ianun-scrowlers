package com.ianun.utils;

import com.ianun.domain.T66yArticle;
import com.ianun.domain.T66yImg;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description: The crawlers utils
 * @date ......: 2020-December-Wednesday
 */
@Controller
@PropertySource({"classpath:config/crawlers.properties"})
@RequestMapping("/test")
public class HttpUtils {

    @Autowired
    private Environment environment;

    @Value("${crawlers.path}")
    private static String crawlersPath;

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
                htmlString = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlString;
    }

    public static void downloadImage(String url, String path) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager).build();
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getConfig());
        try {
            final CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (null != httpResponse && 200 == httpResponse.getStatusLine().getStatusCode()) {
                String filePath = path + getFileName(url);
                File imgFile = new File(path + getFileName(url));
                OutputStream outputStream = new FileOutputStream(imgFile);
                httpResponse.getEntity().writeTo(outputStream);
                System.out.println("download success: " +filePath );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadT66yImg(T66yImg img, String path) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager).build();
        final HttpGet httpGet = new HttpGet(img.getUrl());
        httpGet.setConfig(getConfig());
        try {
            final CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (null != httpResponse && 200 == httpResponse.getStatusLine().getStatusCode()) {

                File folder = new File(path + img.getFolderName() + File.separator);
                if(!folder.exists()) {
                    folder.mkdirs();
                }

                String filePath = path + img.getFolderName() + File.separator + img.getFileName();
                File imgFile = new File(filePath);
                OutputStream outputStream = new FileOutputStream(imgFile);
                httpResponse.getEntity().writeTo(outputStream);
                System.out.println("download success: " +filePath );
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
    }

    @RequestMapping("/prop")
    public void prop() {
        System.out.println(crawlersPath);
        System.out.println(environment.getProperty("t66y.path"));
    }

    @GetMapping("/html")
    public void html() {
        System.out.println(getHtmlContent("https://www.assdrty.com/images/2020/11/30/015391a363732d5696.jpg"));
    }

    @GetMapping("/img")
    public void image() {
        String url = "https://www.assdrty.com/images/2020/11/30/015391a363732d5696.jpg";
    }
}
