package com.taotao.search.service;

import com.taotao.result.SearchResult;

public interface SearchService {
    /**
     * 查询商品信息
     * @param q 查询条件
     * @param page 总页数
     * @param rows 每一页显示条数  默认60个
     * @return SearchResult对象  (商品集合对象，总页数，总记录条数)
     */
    SearchResult search(String q,int page,int rows) throws Exception;
}
