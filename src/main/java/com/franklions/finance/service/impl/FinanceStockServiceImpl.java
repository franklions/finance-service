package com.franklions.finance.service.impl;

import com.franklions.finance.domain.FinanceStockInfo;
import com.franklions.finance.mapper.FinanceStockInfoMapper;
import com.franklions.finance.service.FinanceStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
@Service
public class FinanceStockServiceImpl implements FinanceStockService {

    @Autowired
    FinanceStockInfoMapper mapper;

    @Override
    public void batchSave(List<FinanceStockInfo> dataList) {
        mapper.batchSaveAppend(dataList);
        return ;
    }

    @Override
    public List<FinanceStockInfo> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public void deleteClosedStock(String code) {
        mapper.delete(new FinanceStockInfo(){{setStockCode(code);}});
    }
}
