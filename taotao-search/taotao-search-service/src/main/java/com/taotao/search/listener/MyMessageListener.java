package com.taotao.search.listener;

import com.taotao.pojo.TbItem;
import com.taotao.result.SearchItem;
import com.taotao.search.service.SearchItemService;
import com.taotao.service.ItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

public class MyMessageListener implements MessageListener {

    /**
     * 消费者    生产者在manager-service-itemserviceimpl
     * @param message
     */
    @Autowired
    private SearchItemService itemService;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        //是否为TextMessage
        if (message instanceof TextMessage){}
            TextMessage textMessage = (TextMessage) message;
        try {
            String id = textMessage.getText();
            SearchItem item = itemService.findItemById(Long.valueOf(id));
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
            solrServer.commit();

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
