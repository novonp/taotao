package com.taotao.search.service.impl;

import com.taotao.result.SearchItem;
import com.taotao.result.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import com.taotao.search.service.dao.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult importAllItems() throws Exception{
        //查询数据库得到的结果集
        List<SearchItem> itemList = searchItemMapper.getItemList();
        for (SearchItem item:itemList){
            //为每一个商品创建SolrInputDocument对象
            SolrInputDocument document = new SolrInputDocument();
            //为文档添加域
            document.addField("id", item.getId());
            document.addField("item_title", item.getTitle());
            document.addField("item_sell_point", item.getSell_point());
            document.addField("item_price", item.getPrice());
            document.addField("item_image", item.getImage());
            document.addField("item_category_name", item.getCategory_name());
            document.addField("item_desc", item.getItem_desc());
            //向索引库中添加文档
            solrServer.add(document);
        }
        solrServer.commit();

        return TaotaoResult.ok();
    }

    @Override
    public SearchItem findItemById(Long id) {
        SearchItem searchItem = searchItemMapper.getItemById(id);
        return searchItem;
    }
}
