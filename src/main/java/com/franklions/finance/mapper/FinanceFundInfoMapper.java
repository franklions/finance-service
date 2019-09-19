package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceFundInfo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface FinanceFundInfoMapper extends Mapper<FinanceFundInfo> , InsertListMapper<FinanceFundInfo> {

    @InsertProvider(type = FinanceFundInfoMapperProvider.class,method = "batchSaveAppend")
    void batchSaveAppend(@Param("list") List<FinanceFundInfo> dataList);
}