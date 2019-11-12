package com.franklions.finance.mapper;

import com.franklions.finance.domain.FinanceStockDay;
import org.apache.ibatis.annotations.Param;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-25
 * @since Jdk 1.8
 */
public class FinanceStockDayMapperProvider {

    public static String insertAndAppend(@Param("dayInfo") FinanceStockDay dayInfo){
        StringBuilder sql = new StringBuilder();

        sql.append(" INSERT INTO `finance_stock_day`( `stock_code`, `stock_name`, `company`, `block`,`stock_grade`, `stock_date`, `current`, `stock_open`, `high`, `low`, `stock_close`, `stock_change`, `zgb`, `volume`, `amount`, `fix_total_share`, `cvs`, `ltgb`, `swing`, `turnover`, `pb`, `pe`, `main_in`, `main_out`, `retail_in`, `retail_out`, `is_deleted`, `gmt_create`, `gmt_modified`, `ts`) VALUES (");
        sql.append(" #{dayInfo.stockCode},#{dayInfo.stockName},#{dayInfo.company},#{dayInfo.block},#{dayInfo.stockGrade},#{dayInfo.stockDate},#{dayInfo.current},#{dayInfo.open},#{dayInfo.high},#{dayInfo.low},#{dayInfo.close},#{dayInfo.change},#{dayInfo.zgb},#{dayInfo.volume},#{dayInfo.amount},#{dayInfo.fixTotalShare},#{dayInfo.cvs},#{dayInfo.ltgb},#{dayInfo.swing},#{dayInfo.turnover},#{dayInfo.pb},#{dayInfo.pe},#{dayInfo.mainIn},#{dayInfo.mainOut},#{dayInfo.retailIn},#{dayInfo.retailOut},#{dayInfo.isDeleted},#{dayInfo.gmtCreate},#{dayInfo.gmtModified},#{dayInfo.ts} ");
        sql.append(" ) ON DUPLICATE KEY UPDATE company=VALUES(company),block=VALUES(block),stock_grade=VALUES(stock_grade),current=VALUES(current), stock_open=VALUES(stock_open),high=VALUES(high),low=VALUES(low),stock_close=VALUES(stock_close),stock_change=VALUES(stock_change),zgb=VALUES(zgb),volume=VALUES(volume),amount=VALUES(amount),fix_total_share=VALUES(fix_total_share),cvs=VALUES(cvs),ltgb=VALUES(ltgb),swing=VALUES(swing),turnover=VALUES(turnover),pb=VALUES(pb),pe=VALUES(pe),gmt_modified=VALUES(gmt_modified) ");
        return sql.toString();
    }
}
