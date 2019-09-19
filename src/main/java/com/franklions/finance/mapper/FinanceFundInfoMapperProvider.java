package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceFundInfo;
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
public class FinanceFundInfoMapperProvider {

    public static String batchSaveAppend(@Param("list") List<FinanceFundInfo> dataList){
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO `finance_stock_db`.`finance_fund_info`(`fund_code`, `fund_name`, `fund_type`, " +
                "`is_deleted`, `gmt_create`, `gmt_modified`, `ts`) VALUES ");
        if(dataList !=null &&  dataList.size() > 0)
        {
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].fundCode},#'{'list[{0}].fundName},#'{'list[{0}].fundType}," +
                    "#'{'list[{0}].isDeleted},#'{'list[{0}].gmtCreate},#'{'list[{0}].gmtModified},#'{'list[{0}].ts})");
            for(int i = 0; i< dataList.size();i++){
                sql.append(messageFormat.format(new Integer[]{i}));
                if (i < dataList.size() - 1)
                {
                    sql.append(",");
                }
            }
            sql.append("  ON DUPLICATE KEY UPDATE fund_code=VALUES(fund_code),fund_name=VALUES(fund_name) ,gmt_modified=VALUES(gmt_modified)");
        }
        return sql.toString();
    }
}
