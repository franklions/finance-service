package com.franklions.finance.service.impl;

import com.franklions.finance.domain.FinanceStockDay;
import com.franklions.finance.mapper.FinanceStockDayMapper;
import com.franklions.finance.service.FinanceStockDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-25
 * @since Jdk 1.8
 */
@Service
public class FinanceStockDayServiceImpl implements FinanceStockDayService {

    @Autowired
    FinanceStockDayMapper mapper;

    @Override
    public void save(FinanceStockDay dayInfo) {
        mapper.insertAndAppend(dayInfo);
    }
}
