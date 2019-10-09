package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceStockGrade;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface FinanceStockGradeMapper extends Mapper<FinanceStockGrade>, InsertListMapper<FinanceStockGrade> {
}