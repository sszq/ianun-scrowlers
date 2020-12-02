package com.ianun.service;

import com.ianun.domain.T66yArticle;
import com.ianun.domain.T66yImg;

import java.util.List;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
public interface T66yService {

    List<String> articleUrlList(String url);
    List<String> ImgUrlList(String url);

    List<T66yArticle> articleList(String url);
    List<T66yImg> imgList(T66yArticle article);
}
