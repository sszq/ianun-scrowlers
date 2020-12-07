package com.ianun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("t1")
    public void t1() {
        System.out.println(System.getProperties().getProperty("proxySet"));
        System.out.println(System.getProperties().getProperty("http.proxyHost"));
        System.out.println(System.getProperty("http.proxyPort"));
        System.out.println("打印一下，print: Hello World");
    }
}
