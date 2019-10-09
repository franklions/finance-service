package com.franklions.finance.service;

import com.franklions.finance.domain.FinanceStockGrade;

import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-10-09
 * @since Jdk 1.8
 */
public interface FinanceStockGradeService {
    void batchSave(List<FinanceStockGrade> dataList);
}
