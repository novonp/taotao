package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MyMessageListener  implements MessageListener{

    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    private BufferedWriter writer;

    @Override
    public void onMessage(Message message) {
        //是否为TextMessage
        if (message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            try {
                String id = textMessage.getText();
                TbItem tbItem = itemService.findTbItemById(Long.valueOf(id));
                //商品基本信息
                Item item = new Item(tbItem);
                //商品描述信息
                TbItemDesc itemDesc = itemService.findItemDescByItemId(Long.valueOf(id));
                Map map = new HashMap();
                map.put("item",item);
                map.put("itemDesc",itemDesc);
                Configuration configuration = freeMarkerConfig.getConfiguration();
                Template template = configuration.getTemplate("item.ftl");
                writer = new BufferedWriter(new FileWriter("E:\\vov\\"+id+".html"));
                template.process(map,writer);
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (MalformedTemplateNameException e) {
                e.printStackTrace();
            } catch (TemplateNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }finally {
                if (writer!=null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /**
         * 1.创建一个Configuration对象。
         * 	Configuration configuration = new Configuration();
         * 	2.设置模板文件路径(.ftl结尾)
         * 	configuration .setDirectoryForTemplateLoading(new File((pathname:"路径"))
         * 	3.设置模板文件字符集
         * 	configuration.setDefaultEncoding("UTF-8");
         * 	4.加载一个模板，创建一个模板对象
         * 	Template template = configuration.getTemplate(name:"名称＋后缀");
         * 	5.创建一个模板使用的数据集
         * 	Map map = new HashMap();
         * 	map.put("名称","测试用的");
         * 	Writer writer = new FileWriter(new File(pathname:"存放地址+名称"))；
         * 	template.process(map,writer);
         * 	writer.close();
         */
    }
}
