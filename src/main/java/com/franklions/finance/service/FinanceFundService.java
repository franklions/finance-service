package com.franklions.finance.service;

import com.franklions.finance.domain.FinanceFundInfo;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
public interface FinanceFundService {
    void batchSave(List<FinanceFundInfo> dataList);
}
