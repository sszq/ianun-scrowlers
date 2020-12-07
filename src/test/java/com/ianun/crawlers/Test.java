package com.ianun.crawlers;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-03
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(System.getProperties().getProperty("proxySet"));
        System.out.println(System.getProperties().getProperty("http.proxyHost"));
        System.out.println(System.getProperty("http.proxyPort"));
    }
}
