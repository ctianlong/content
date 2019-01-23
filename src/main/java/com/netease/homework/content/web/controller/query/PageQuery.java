package com.netease.homework.content.web.controller.query;

import java.io.Serializable;

/**
 * @Description 封装通用分页查询参数
 * @Auther ctl
 * @Date 2018/7/21
 */
public class PageQuery implements Serializable {
    private static final long serialVersionUID = -8412326181645278066L;
    private int pageNumber = 1;
    private int pageSize = 10;
    private String sortName;
    private String sortOrder;
    private String searchText;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
