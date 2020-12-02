package com.ianun.controller;

import com.ianun.domain.T66yArticle;
import com.ianun.domain.T66yImg;
import com.ianun.service.impl.T66yServiceImpl;
import com.ianun.thread.T66yObjThread;
import com.ianun.thread.T66yThread;
import com.ianun.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

    @RequestMapping("/alist")
    public void articleUrlList(String url) {
        url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        List<String> list = t66yService.articleUrlList(url);

        for (String value : list) {
            System.out.println(value);
        }

    }

    @RequestMapping("ilist")
    public void imgUrlList(String url) {
        url = "http://t66y.com/htm_data/2012/16/4207146.html";
        List<String> list = t66yService.ImgUrlList(url);

        for (String value : list) {
            System.out.println(value);
        }
    }

    @RequestMapping("/all")
    public void allImgUrl(String url) {
        url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        List<String> list = t66yService.articleUrlList(url);
        for (String value : list) {
            System.out.println("================= " + value + "=================");
           List<String> imgList = t66yService.ImgUrlList(value);
            for (String s : imgList) {
                System.out.println(s);
            }
        }
    }

    @RequestMapping("/downloadold")
    public void downloadOld(String url) {
        url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        List<String> list = t66yService.articleUrlList(url);
        int i = 0;
        for (String value : list) {
            Thread thread = new T66yThread(value, path);
            thread.start();
            System.out.println("Thread " + ++i + "start: " + value);
        }
    }

    @RequestMapping("/download")
    public void download(String url) {
        url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        List<T66yArticle> articleList = t66yService.articleList(url);
        int i = 0;
        for (T66yArticle article : articleList) {
            Thread thread = new T66yObjThread(article, path);
            thread.start();
            System.out.println("Thread " + ++i + "start: " + article.getTitle());
        }
    }

}
