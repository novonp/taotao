package com.taotao.search.service.dao;

import com.taotao.result.SearchItem;
import com.taotao.result.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery query) throws SolrServerException {
        SearchResult result = new SearchResult();
        //商品集合对象
        List<SearchItem> items = new ArrayList<SearchItem>();
        QueryResponse response = solrServer.query(query);
        SolrDocumentList documentList = response.getResults();
        //总记录条数
        result.setRecordCount(documentList.getNumFound());
        //得到高亮的域 集合
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //得到所有的文档对象
        for(SolrDocument document:documentList){
            SearchItem item = new SearchItem();
            item.setId((String) document.get("id"));
            item.setCategory_name((String) document.get("item_category_name"));
            item.setImage((String) document.get("item_image"));
            item.setPrice((Long)document.get("item_price"));
            item.setSell_point((String) document.get("item_sell_point"));
            item.setItem_desc((String) document.get("item_desc"));

            //为那个字段添加高亮
            List<String> list = highlighting.get(document.get("id")).get("item_title");

            if (list != null && list.size() > 0){
                //如果设置了高亮则从集合里面取出数据， 集合对象只有一个，集合里面的数据有两个
                item.setTitle(list.get(0));
            }else {
                item.setTitle((String) document.get("item_title"));
            }
            //把从solr里面查询出来的数据存放到集合对象里面去
            items.add(item);

        }
        //设置集合
        result.setItemList(items);

        return result;
    }
}
