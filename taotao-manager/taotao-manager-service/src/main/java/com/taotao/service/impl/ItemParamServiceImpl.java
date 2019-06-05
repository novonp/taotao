package com.taotao.service.impl;

import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Override
    public TaotaoResult getItemParamByCid(Long itemCatId) {
        TbItemParam param = tbItemParamMapper.findItemParamByCid(itemCatId);
        return TaotaoResult.ok(param);
    }

    @Override
    public TaotaoResult addItemParam(TbItemParam tbItemParam) {
        Date date = new Date();
        tbItemParam.setCreated(date);
        tbItemParam.setUpdated(date);
        tbItemParamMapper.insertItemParam(tbItemParam);
        return TaotaoResult.ok();
    }
}
