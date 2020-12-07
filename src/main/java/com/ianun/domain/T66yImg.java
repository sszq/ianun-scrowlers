package com.ianun.domain;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-02
 */
public class T66yImg {

    private String url;
    private String fileName;
    private String listName;
    private String folderName;
    private String parentUrl;
    private T66yArticle parent;

    public T66yArticle getParent() {
        return parent;
    }

    public void setParent(T66yArticle parent) {
        this.parent = parent;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    @Override
    public String toString() {
        return "T66yImg{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", listName='" + listName + '\'' +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
