package com.ianun.service.impl;

import com.ianun.dao.T66yDao;
import com.ianun.domain.T66yArticle;
import com.ianun.domain.T66yImg;
import com.ianun.service.T66yService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
@Service
public class T66yServiceImpl implements T66yService {

    @Autowired
    T66yDao t66yDao;

    @Override
    public List<String> articleUrlList(String url) {
        return t66yDao.articleUrlList(url);
    }

    @Override
    public List<String> ImgUrlList(String url) {
        return t66yDao.imgUrlList(url);
    }

    @Override
    public List<T66yArticle> articleList(String url) {
        return t66yDao.articleList(url);
    }

    @Override
    public List<T66yImg> imgList(T66yArticle article) {
        return t66yDao.imgList(article);
    }
}
