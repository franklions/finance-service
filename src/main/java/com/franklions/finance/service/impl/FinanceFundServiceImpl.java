package com.franklions.finance.service.impl;

import com.franklions.finance.domain.FinanceFundInfo;
import com.franklions.finance.mapper.FinanceFundInfoMapper;
import com.franklions.finance.service.FinanceFundService;
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
public class FinanceFundServiceImpl implements FinanceFundService {

    @Autowired
    FinanceFundInfoMapper mapper;

    @Override
    public void batchSave(List<FinanceFundInfo> dataList) {
        mapper.batchSaveAppend(dataList);
        return ;
    }
}
