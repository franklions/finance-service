package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceStockInfo;
import org.apache.ibatis.annotations.Param;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
public class FinanceStockInfoMapperProvider {

    public static String batchSaveAppend(@Param("list") List<FinanceStockInfo> dataList){
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO `finance_stock_info`(`stock_code`, `stock_name`, `stock_type`, " +
                "`is_deleted`, `gmt_create`, `gmt_modified`, `ts`) VALUES ");
        if(dataList !=null &&  dataList.size() > 0)
        {
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].stockCode},#'{'list[{0}].stockName},#'{'list[{0}].stockType}," +
                    "#'{'list[{0}].isDeleted},#'{'list[{0}].gmtCreate},#'{'list[{0}].gmtModified},#'{'list[{0}].ts})");
            for(int i = 0; i< dataList.size();i++){
                sql.append(messageFormat.format(new Integer[]{i}));
                if (i < dataList.size() - 1)
                {
                    sql.append(",");
                }
            }
            sql.append("  ON DUPLICATE KEY UPDATE stock_code=VALUES(stock_code),stock_name=VALUES(stock_name) ,gmt_modified=VALUES(gmt_modified)");
        }
        return sql.toString();
    }
}
