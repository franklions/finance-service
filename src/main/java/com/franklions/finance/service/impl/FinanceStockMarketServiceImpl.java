package com.franklions.finance.service.impl;

import com.franklions.finance.domain.FinanceStockMarket;
import com.franklions.finance.mapper.FinanceStockMarketMapper;
import com.franklions.finance.service.FinanceStockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-11-02
 * @since Jdk 1.8
 */
@Service
public class FinanceStockMarketServiceImpl implements FinanceStockMarketService {

    @Autowired
    FinanceStockMarketMapper marketMapper;

    @Override
    public void save(FinanceStockMarket market) {
        marketMapper.insertAndAppend(market);
    }
}
