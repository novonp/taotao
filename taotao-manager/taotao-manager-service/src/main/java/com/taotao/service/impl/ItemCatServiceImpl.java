package com.taotao.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.result.EasyUITreeNode;
import com.taotao.result.ItemCat;
import com.taotao.result.ItemCatResult;
import com.taotao.service.ItemCatService;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getCatList(long parentId) {
        List<TbItemCat> catList = tbItemCatMapper.findTbTtemCatParentId(parentId);
        List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
        for (TbItemCat itemCat:catList) {
            EasyUITreeNode node = new EasyUITreeNode();
            //设置id
            node.setId(itemCat.getId());
            //设置名称
            node.setText(itemCat.getName());
            //设置状态
            node.setState(itemCat.getIsParent()?"closed":"open");
            result.add(node);
        }
        return result;
    }

    /**
     * 商品首页分类展示
     * @param parentId
     * @return
     */
    @Override
    public ItemCatResult findItemCatAll(long parentId) {
        //因为返回ItemCatResult对象 里面有14个集合数据 这14个集合对象就是展示分类信息的数据
        ItemCatResult result = new ItemCatResult();
        result.setData(getItemCatAll(parentId));
        return result;
    }
    //完成数据的装载
    public List<ItemCat> getItemCatAll(long parentId){

        //这里要加入缓存 (有时间再写)

        List result = new ArrayList();
        //得到所有的分类信息
        List<TbItemCat> itemCats = tbItemCatMapper.findTbTtemCatParentId(parentId);

        int count = 0;

        for (TbItemCat itemCat:itemCats) {
            ItemCat item = new ItemCat();

            //是否为父级目录
            if (itemCat.getIsParent()){
                ///products/1.html 1:id      设置 url
                item.setUrl("/products/"+itemCat.getId()+".html");

                if(parentId==0){
                    //设置 name
                    item.setName("<a href='/products/"+itemCat.getId()+".html'>"+itemCat.getName()+"</a>");
                }else {
                    //第二级目录
                    item.setName(itemCat.getName());
                }

                count++;

                //设置 i    设置递归 (如果要递归说明他不是子目录)
                item.setItem(getItemCatAll(itemCat.getId()));
                //把一级目录二级目录添加到返回结果集 list集合里面取
                result.add(item);

                //页面左侧导航栏有18个一级目录，只能显示14个一级目录
                if (parentId == 0 && count >= 14){
                    break;
                }

            }else {
                //子目录(字符串形式)
                result.add("/products/"+itemCat.getId()+".html|"+itemCat.getName());
            }

        }

        return result;
    }
}
