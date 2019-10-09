package com.franklions.finance.service.impl;

import com.franklions.finance.domain.FinanceStockGrade;
import com.franklions.finance.mapper.FinanceStockGradeMapper;
import com.franklions.finance.service.FinanceStockGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-10-09
 * @since Jdk 1.8
 */
@Service
public class FinanceStockGradeServiceImpl implements FinanceStockGradeService {

    @Autowired
    FinanceStockGradeMapper mapper;

    @Override
    public void batchSave(List<FinanceStockGrade> dataList) {
        mapper.insertList(dataList);
    }
}
