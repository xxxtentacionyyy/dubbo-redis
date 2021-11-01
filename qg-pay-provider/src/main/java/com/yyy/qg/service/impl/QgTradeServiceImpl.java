package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yyy.qg.mapper.QgTradeMapper;
import com.yyy.qg.pojo.QgTrade;
import com.yyy.qg.service.QgTradeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Service
@Component
public class QgTradeServiceImpl implements QgTradeService {
    @Resource
    private QgTradeMapper qgTradeMapper;
    public QgTrade getQgTradeById(String id)throws Exception{
        return qgTradeMapper.getQgTradeById(id);
    }
    public List<QgTrade> getQgTradeListByMap(Map<String,Object> param)throws
            Exception{
        return qgTradeMapper.getQgTradeListByMap(param);
    }
    public Integer getQgTradeCountByMap(Map<String,Object> param)throws
            Exception{
        return qgTradeMapper.getQgTradeCountByMap(param);
    }
    public Integer qdtxAddQgTrade(QgTrade qgTrade)throws Exception{
        qgTrade.setCreatedTime(new Date());
        return qgTradeMapper.insertQgTrade(qgTrade);
    }
    public Integer qdtxModifyQgTrade(QgTrade qgTrade)throws Exception{
        qgTrade.setUpdatedTime(new Date());
        return qgTradeMapper.updateQgTrade(qgTrade);
    }
    public Integer qdtxDeleteQgTradeById(String id)throws Exception{
        return qgTradeMapper.deleteQgTradeById(id);
    }
    public Integer qdtxBatchDeleteQgTrade(String ids)throws Exception{
        Map<String,List<String>> param=new HashMap<String,List<String>>();
        String[] paramArrays=ids.split(",");
        List<String> idList=new ArrayList<String>();
        for (String temp:paramArrays){
            idList.add(temp);
        }
        param.put("ids",idList);
        return qgTradeMapper.batchDeleteQgTrade(param);
    }
}
