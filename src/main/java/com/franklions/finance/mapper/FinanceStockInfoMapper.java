package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceStockInfo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface FinanceStockInfoMapper extends Mapper<FinanceStockInfo> , InsertListMapper<FinanceStockInfo> {

    @InsertProvider(type = FinanceStockInfoMapperProvider.class,method = "batchSaveAppend")
    void batchSaveAppend(@Param("list") List<FinanceStockInfo> dataList);
}