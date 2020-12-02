package com.ianun.thread;

import com.ianun.dao.T66yDao;
import com.ianun.domain.T66yArticle;
import com.ianun.domain.T66yImg;
import com.ianun.utils.HttpUtils;

import java.util.List;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
public class T66yObjThread extends Thread {

    T66yDao t66yDao = new T66yDao();

    private T66yArticle article;
    private String path;

    public T66yObjThread(T66yArticle article, String path) {
        this.article = article;
        this.path = path;
    }

    @Override
    public void run() {
        final List<T66yImg> imgList = t66yDao.imgList(article);
        for (T66yImg img : imgList) {
            HttpUtils.downloadT66yImg(img, path);
        }
    }
}
