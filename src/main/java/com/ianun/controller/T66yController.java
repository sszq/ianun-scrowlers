package com.ianun.controller;

import com.ianun.domain.T66yArticle;
import com.ianun.service.impl.T66yServiceImpl;
import com.ianun.thread.T66yImgThread;
import com.ianun.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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

    @Value("${t66y.path.save}")
    private String t66yPathSave;

    @Value("${t66y.url.home}")
    private String t66yUrlHome;

    @Value("${t66y.url.demo.category}")
    private String t66yUrlDemoCategory;

    @Value("${t66y.url.demo.article}")
    private String t66yUrlDemoArticle;

    /** get properties value from application.properties */
    @Autowired
    Environment environment;


    @RequestMapping("/html")
    public Object html(String url) {
        if (null == url) {
            url = t66yUrlHome;
        }
        return HttpUtils.getHtmlContent(url);
    }

    @RequestMapping("/img")
    public Object img(String url) {
        HttpUtils.downloadImage(url, t66yPathSave);
        return "success";
    }

    @RequestMapping("/list/article")
    public void articleUrlList(String url) {
        if (null == url) {
            url = t66yUrlDemoCategory;
        }
        List<String> list = t66yService.articleUrlList(url);

        for (String value : list) {
            System.out.println(value);
        }
    }

    @RequestMapping("/list/img")
    public void imgUrlList(String url) {
        if (null == url) {
            url = t66yUrlDemoArticle;
        }
        List<String> list = t66yService.ImgUrlList(url);

        for (String value : list) {
            System.out.println(value);
        }
    }

    @RequestMapping("/list/all")
    public void allImgUrl(String url) {
        if (null == url) {
            url = t66yUrlDemoCategory;
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
        String baseUrl = "http://t66y.com/thread0806.php?%s&%s";
        String url = String.format(baseUrl, fid, page);
        List<T66yArticle> list = t66yService.articleList(url);
        int i = 0;
        for (T66yArticle article : list) {
            Thread thread = new T66yImgThread(article, t66yPathSave);
            thread.start();
            System.out.println("Thread " + ++i + "start: " + article.getTitle());
        }
        System.out.println("******************* 下载结束 ********************");
    }
}
