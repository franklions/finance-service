package com.franklions.finance.service;

import com.franklions.finance.domain.FinanceStockInfo;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
public interface FinanceStockService {
    void batchSave(List<FinanceStockInfo> dataList);
}
