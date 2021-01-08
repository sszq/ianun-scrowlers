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
import java.util.Locale;

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

                    if (url.contains("fid=20")) {
                        article.setTitleType(td1.text());
                    }

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
                                article.setParentUrl(url);
                                list.add(article);
                                // 达盖尔的旗帜
                            } else if (url.contains("fid=16")) {
                                Element font = a.selectFirst("font");
                                // page1
                                if (null != font) {
                                    article.setTitle(font.text());
                                    article.setParentUrl(url);
                                    list.add(article);
                                // page2,3,4,...
                                } else {
                                    article.setTitle(a.text());
                                    article.setParentUrl(url);
                                    list.add(article);
                                }
                                // 成人文学交流区
                            } else if (url.contains("fid=20")) {
                                article.setTitle(article.getTitleType() + a.text());
                                article.setParentUrl(url);
                                list.add(article);
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

    public T66yArticle novelContent(T66yArticle article) {

        StringBuilder contents = new StringBuilder();
        try {
            Document document = Jsoup.parse(new URL(article.getUrl()), 40000);
            String title = document.selectFirst("title").text();
            if (title.contains(" - 成人文學交流區")) {
                article.setTitle(title.substring(0, title.indexOf(" - 成人文學交流區")));
            }

            /**
             * 判断是否还有下一页
             * 1. 如果有继续查找
             * 2. 如果没有则返回
             */

            boolean hasNextPage = true;
            String nextUrl = article.getUrl();

            while (hasNextPage) {
                String content = getPieceByPage(nextUrl);
                contents.append(content);

                hasNextPage = hasNextPage(nextUrl);

                if (!hasNextPage) {
                    break;
                }

                nextUrl = nextPageUrl(nextUrl);
                if (null == nextUrl) {
                    continue;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        article.setNovelContent(contents.toString());

        return article;
    }

    static String getPieceByPage(String url) {
        StringBuilder pieces = new StringBuilder();
        try {
            Document document = Jsoup.parse(new URL(url), 11000);
            // 第1页的文章内容存在于这样的class内
            Elements elements = document.select("div[class=tpc_content do_not_catch]");

            // 非第1页的文章内容存在这样的class内
            if (!url.endsWith("html")) {
                elements = document.select("div[class=tpc_content]");
            }

            System.out.println("content size:" + url + ", " + elements.size());
            int i = 0;
            for (Element element : elements) {
                String piece = element.html();
                piece = piece.replace("<br>", "");
                // 小于100长度多为评论，不写入
                if (piece.length() > 100) {
                    pieces.append(piece);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("content size:" + url + ", " + pieces.toString().length());
        return pieces.toString();
    }

    boolean hasNextPage(String url) {
        boolean hasNextPage = false;
        final Document document;
        Element last = null;
        String gray = null;
        try {
            document = Jsoup.parse(new URL(url), 40000);
            last = document.selectFirst("a#last.gray");
            if (null == last) {
                hasNextPage = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hasNextPage;
    }

    String nextPageUrl(String url) {
        final Document document;
        String href = null;
        try {
            document = Jsoup.parse(new URL(url), 40000);
            final Element element = document.selectFirst("a#last");
            if (null == element) {
                return null;
            }
            System.out.println(element.html());
            href = element.attr("href");
            // 对于第1页的【下一页】 href内容为<a href="../../../read.php?tid=4216872&amp;page=2" id="last">＞</a>
            if (href.startsWith("../")) {
                href = href.substring(9, href.length());
            }

            System.out.println("href:" + href);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://t66y.com/" + href;
    }

}
