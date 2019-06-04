package com.taotao.search.service.impl;

import com.taotao.result.SearchResult;
import com.taotao.search.service.SearchService;
import com.taotao.search.service.dao.SearchDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String q, int page, int rows) throws Exception {
        //创建查询对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(q);
        //设置默认域
        query.set("df","item_keywords");
        //开始索引规则   (当前页  -  1) * rows
        //开始索引从 0 开始
        query.setStart((page-1)*rows);
        //显示记录条数
        query.setRows(rows);
        //开启  设置高亮选项
        query.setHighlight(true);
        //设置那个域的高亮 显示
        query.addHighlightField("item_title");
        //头标签
        query.setHighlightSimplePre("<em style='color:red'>");
        //尾标签
        query.setHighlightSimplePost("</em>");

        SearchResult result = searchDao.search(query);

        //总页数  计算规则  (总记录条数 % 每一页显示的条数 == 0 ? 总记录条数 / 每一页显示的条数 : 总记录条数 / 每一页显示的条数 + 1)
        result.setPageCount(result.getRecordCount()%rows==0?result.getRecordCount()/rows:result.getRecordCount()/rows+1);

        return result;
    }
}
