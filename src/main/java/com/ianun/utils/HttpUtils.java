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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
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
                .setConnectTimeout(100 * 1000)
                // The max long time of Getting connection
                .setConnectionRequestTimeout(100 * 1000)
                // The max long time of data transfer
                .setSocketTimeout(100 * 1000)
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
        httpGet.setHeader("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0");
        httpGet.setConfig(getConfig());
        try {
            final CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (null != httpResponse && 200 == httpResponse.getStatusLine().getStatusCode()) {
                File folder = new File(path + img.getFolderName() + File.separator);
                // 新时代的我们
                if (img.getParent().getParentUrl().contains("fid=8")) {
                    folder = new File(path + "新时代的我们" + File.separator + img.getFolderName() + File.separator);
                    path += "新时代的我们" + File.separator;
                } else if (img.getParent().getParentUrl().contains("fid=16")) {
                    // 达盖尔的旗帜
                    folder = new File(path + "达盖尔的旗帜" + File.separator + img.getFolderName() + File.separator);
                    path += "达盖尔的旗帜" + File.separator;
                }
                if(!folder.exists()) {
                    folder.mkdirs();
                }

                String filePath = path + img.getFolderName() + File.separator + img.getFileName();
                File imgFile = new File(filePath);
                OutputStream outputStream = new FileOutputStream(imgFile);
                httpResponse.getEntity().writeTo(outputStream);
                System.out.println("download success: " + img.getUrl() + "\t" +filePath );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadT66yNovel(T66yArticle article, String path) {

        try {
            String square = path + "成人文学交流区" + File.separator;
            String file = square + article.getTitle() + ".txt";

            File squareFolder = new File(square);
            if(!squareFolder.exists()) {
                squareFolder.mkdirs();
            }

            System.out.println(article.getUrl() + ": " + file);

            OutputStream os = new FileOutputStream(file);
            String content = article.getNovelContent();
            if (null != content) {
                os.write(content.getBytes("UTF-8"));
            }
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

        String str = "[現代奇幻] 浮华背后 01-24完 - 成人文學交流區 | 草榴社區 - t66y.com";

        int index = str.indexOf(" - 成人文學交流區");

        System.out.println(str.substring(0, index));
    }


}
