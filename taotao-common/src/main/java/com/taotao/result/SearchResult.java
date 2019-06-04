package com.taotao.result;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    //商品集合 默认情况下  集合有60个
    private List<SearchItem> itemList;
    //总记录条数
    private Long recordCount;
    //总页数
    private Long pageCount;

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "itemList=" + itemList +
                ", recordCount=" + recordCount +
                ", pageCount=" + pageCount +
                '}';
    }
}
