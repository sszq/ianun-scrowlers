package com.ianun.dao;

import com.ianun.domain.T66yArticle;
import com.ianun.domain.T66yImg;
import com.ianun.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
@Component
public class T66yDao {

    public List<String> articleUrlList(String url) {
        List<String> list = new ArrayList<>();
//        url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        final Document document;
        try {
            document = Jsoup.parse(new URL(url), 4000);
            final Elements elements = document.select("tr[class=tr3 t_one tac]");
            for (Element element : elements) {
                Element td = element.getElementsByTag("td").get(1);
                if (null != td) {
                   Element h3 = td.getElementsByTag("h3").first();
                   if (null != h3) {
                       Element a = h3.getElementsByTag("a").first();
                       if (null != a) {
                           String href = a.attr("href");
                           href = "http://t66y.com/" + href;
                           list.add(href);
                       }
                   }

                }
            }
            System.out.println(elements.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> imgUrlList(String url) {
        List<String> list = new ArrayList<>();
        try {
            final Document document = Jsoup.parse(new URL(url), 11000);
            final Elements elements = document.select("img[iyl-data=http://a.d/adblo_ck.jpg]");
            System.out.println("elements.size():" + elements.size());
            for (Element element : elements) {
                String src = element.getElementsByAttribute("ess-data").attr("ess-data");
                list.add(src);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<T66yArticle> articleList(String url) {
        List<T66yArticle> list = new ArrayList<>();
//        url = "http://t66y.com/thread0806.php?fid=16&search=&page=1";
        final Document document;
        try {
            document = Jsoup.parse(new URL(url), 4000);
            final Elements elements = document.select("tr[class=tr3 t_one tac]");
            for (Element element : elements) {
                T66yArticle article = new T66yArticle();
                Element td0 = element.getElementsByTag("td").get(0);
                if (null != td0) {
                    Element span = td0.selectFirst("span");
                    if (null != span) {
                        String likes = span.text();
                        if (null != likes) {
                            article.setLikes(Integer.valueOf(likes));
                        }
                    }
                }

                Element td1 = element.getElementsByTag("td").get(1);
                if (null != td1) {
                    Element h3 = td1.getElementsByTag("h3").first();
                    if (null != h3) {
                        Element a = h3.getElementsByTag("a").first();
                        if (null != a) {
                            String href = a.attr("href");
                            href = "http://t66y.com/" + href;
                            article.setUrl(href);
                            // 新时代的我们
                            if (url.contains("fid=8")) {
                                article.setTitle(a.text());
                                list.add(article);
                                // 达盖尔的旗帜
                            } else if (url.contains("fid=16")) {
                                Element font = a.selectFirst("font");
                                if (null != font) {
                                    article.setTitle(font.text());
                                    article.setParentUrl(url);
                                    list.add(article);
                                }
                            }
                        }
                    }

                }
            }
            System.out.println(elements.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<T66yImg> imgList(T66yArticle article) {
        List<T66yImg> list = new ArrayList<>();
        try {
            final Document document = Jsoup.parse(new URL(article.getUrl()), 11000);
            final Elements elements = document.select("img[iyl-data=http://a.d/adblo_ck.jpg]");
            System.out.println("elements.size():" + elements.size());
            for (Element element : elements) {
                T66yImg img = new T66yImg();
                String src = element.getElementsByAttribute("ess-data").attr("ess-data");
                img.setUrl(src);
                img.setFolderName(article.getTitle());
                img.setFileName(HttpUtils.getFileName(img.getUrl()));
                img.setParentUrl(article.getUrl());
                img.setParent(article);
                list.add(img);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
