package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceStockMarket;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface FinanceStockMarketMapper extends Mapper<FinanceStockMarket> {

    @InsertProvider(type = FinanceStockMarketMapperProvider.class,method = "insertAndAppend")
    void insertAndAppend(@Param("market") FinanceStockMarket market);
}