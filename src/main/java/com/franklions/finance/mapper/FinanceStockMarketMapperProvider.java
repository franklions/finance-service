package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceStockMarket;
import org.apache.ibatis.annotations.Param;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-11-02
 * @since Jdk 1.8
 */
public class FinanceStockMarketMapperProvider {

    public static String insertAndAppend(@Param("market") FinanceStockMarket market){
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO `finance_stock_db`.`finance_stock_market`(`stock_code`, `stock_name`, `stock_date`, `current`, `stock_open`, `high`, `low`, `stock_close`, `stock_change`, `stock_change_pct`, `volume`, `amount`, `swing`, `is_deleted`, `gmt_create`, `gmt_modified`, `ts`) VALUES ( ");
        sql.append(" #{market.stockCode},#{market.stockName},#{market.stockDate},#{market.current},#{market.open},#{market.high},#{market.low},#{market.close},#{market.change},#{market.changePct},#{market.volume},#{market.amount},#{market.swing},#{market.isDeleted},#{market.gmtCreate},#{market.gmtModified},#{market.ts} ");
        sql.append(" ) ON DUPLICATE KEY UPDATE current=VALUES(current), stock_open=VALUES(stock_open),high=VALUES(high),low=VALUES(low),stock_close=VALUES(stock_close),stock_change=VALUES(stock_change),volume=VALUES(volume),amount=VALUES(amount),swing=VALUES(swing),gmt_modified=VALUES(gmt_modified) ");
        return sql.toString();
    }
}
