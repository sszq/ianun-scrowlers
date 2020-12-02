package com.ianun.thread;

import com.ianun.dao.T66yDao;
import com.ianun.service.impl.T66yServiceImpl;
import com.ianun.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
public class T66yThread extends Thread {

    T66yDao t66yDao = new T66yDao();

    private String path;

    private String url;

    public T66yThread(String url, String path) {
       this.url = url;
       this.path = path;
    }

    @Override
    public void run() {
        final List<String> list = t66yDao.imgUrlList(url);
        for (String value : list) {
            HttpUtils.downloadImage(value, path);
        }
    }
}
