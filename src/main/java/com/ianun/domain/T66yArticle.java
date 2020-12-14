package com.ianun.domain;

import java.util.List;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
public class T66yArticle {

    // 网络地址
    private String url;
    // 文章标题
    private String title;
    // 作者
    private String author;
    // 回复数
    private int replies;
    // 点赞数
    private int likes;
    // 是否已爬取 1 已爬取（下载完所有内容）；未攫取 （未完全下载内容）
    private int crawled;

    private int quantity;

    private String parentUrl;

    private List<T66yImg> t66yImages;

    private String novelContent;

    public String getNovelContent() {
        return novelContent;
    }

    public void setNovelContent(String novelContent) {
        this.novelContent = novelContent;
    }

    /** 用于小说标题前的分类 */
    private String titleType;

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public List<T66yImg> getT66yImages() {
        return t66yImages;
    }

    public void setT66yImages(List<T66yImg> t66yImages) {
        this.t66yImages = t66yImages;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int setQuantity(String url) {
        int a1 = url.indexOf("[");
        int a2 = url.lastIndexOf("]");
        return Integer.parseInt(url.substring(a1 + 1, a2));
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCrawled() {
        return crawled;
    }

    public void setCrawled(int crawled) {
        this.crawled = crawled;
    }

    private List<T66yImg> imageList;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<T66yImg> getImageList() {
        return imageList;
    }

    public void setImageList(List<T66yImg> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "T66yList{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", replies=" + replies +
                ", likes=" + likes +
                ", crawled=" + crawled +
                ", quantity=" + quantity +
                ", t66yImages=" + t66yImages +
                ", imageList=" + imageList +
                '}';
    }
}
