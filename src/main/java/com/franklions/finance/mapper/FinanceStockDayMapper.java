package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceStockDay;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface FinanceStockDayMapper extends Mapper<FinanceStockDay> {

    @InsertProvider(type = FinanceStockDayMapperProvider.class,method = "insertAndAppend")
    void insertAndAppend(@Param("dayInfo") FinanceStockDay dayInfo);
}