package com.ianun.controller;

import com.ianun.domain.T66yArticle;
import com.ianun.service.impl.T66yServiceImpl;
import com.ianun.thread.T66yImgThread;
import com.ianun.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
@RestController
@RequestMapping("/t66y")
@PropertySource({"classpath:config/crawlers.properties"})
public class T66yController {

    @Autowired
    T66yServiceImpl t66yService;

    @Value("${t66y.path}")
    private String path;

    @RequestMapping("/html")
    public Object html(String url) {
        return HttpUtils.getHtmlContent(url);
    }

    @RequestMapping("/img")
    public Object img(String url) {
        HttpUtils.downloadImage(url, path);
        return "success";
    }

    @RequestMapping("/list/article")
    public void articleUrlList(String url) {
        if (null == url) {
            url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        }
        List<String> list = t66yService.articleUrlList(url);

        for (String value : list) {
            System.out.println(value);
        }
    }

    @RequestMapping("/list/img")
    public void imgUrlList(String url) {
        if (null == url) {
            url = "http://t66y.com/htm_data/2012/16/4207146.html";
        }
        List<String> list = t66yService.ImgUrlList(url);

        for (String value : list) {
            System.out.println(value);
        }
    }

    @RequestMapping("/list/all")
    public void allImgUrl(String url) {
        if (null == url) {
            url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        }
        List<String> list = t66yService.articleUrlList(url);
        for (String value : list) {
            System.out.println("================= " + value + "=================");
           List<String> imgList = t66yService.ImgUrlList(value);
            for (String s : imgList) {
                System.out.println(s);
            }
        }
    }

    /**
     * @param fid the category of t66y content
     *            7     技术讨论区
     *            8     新时代的我们
     *            16    达盖尔的旗帜
     *            20    成人文学交流区
     * @param page the page of one category
     */
    @RequestMapping("/dl/{fid}/{page}")
    public void parameter(@PathVariable String fid, @PathVariable String page) {
        // url = "http://t66y.com/thread0806.php?fid=16&page=1";
        String baseUrl = "http://t66y.com/thread0806.php?%s&%s";
        String url = String.format(baseUrl, fid, page);
        List<T66yArticle> list = t66yService.articleList(url);
        int i = 0;
        for (T66yArticle article : list) {
            Thread thread = new T66yImgThread(article, path);
            thread.start();
            System.out.println("Thread " + ++i + "start: " + article.getTitle());
        }
        System.out.println("******************* 下载结束 ********************");
    }
}
