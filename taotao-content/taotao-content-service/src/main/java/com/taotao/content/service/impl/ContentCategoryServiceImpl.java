package com.taotao.content.service.impl;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.result.EasyUITreeNode;
import com.taotao.result.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        List<TbContentCategory> contentCategorys = tbContentCategoryMapper.findTbContentCategoryById(parentId);
        List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
        for (TbContentCategory tbcontent:contentCategorys) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbcontent.getId());
            node.setText(tbcontent.getName());
            node.setState(tbcontent.getIsParent()?"closed":"open");
            result.add(node);
        }
        return result;
    }

    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        Date time = new Date();
        contentCategory.setCreated(time);
        contentCategory.setUpdated(time);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        //不管在哪儿添加他都是一个子节点(不是父节点)
        contentCategory.setIsParent(false);

        //添加一个内容分类
        tbContentCategoryMapper.insert(contentCategory);

        //查询添加的节点父类是否是 (子节点) 如果是就改为父节点， 如果不是就不用管他
        TbContentCategory contentNode = tbContentCategoryMapper.findContentCategoryByParentId(parentId);

        if (!contentNode.getIsParent()){
            contentNode.setIsParent(true);
            //更新到数据库中
            tbContentCategoryMapper.updateCategoryisparentId(contentNode);
        }

        return TaotaoResult.ok(contentCategory);
    }
}
