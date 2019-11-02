package com.franklions.finance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "finance_stock_market")
public class FinanceStockMarket {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 代码
     */
    @Column(name = "stock_code")
    private String stockCode;

    /**
     * 名称
     */
    @Column(name = "stock_name")
    private String stockName;

    /**
     * 日期
     */
    @Column(name = "stock_date")
    private String stockDate;

    /**
     * 当前价
     */
    private BigDecimal current;

    /**
     * 开盘
     */
    @Column(name = "stock_open")
    private BigDecimal open;

    /**
     * 最高
     */
    private BigDecimal high;

    /**
     * 最低
     */
    private BigDecimal low;

    /**
     * 收盘
     */
    @Column(name = "stock_close")
    private BigDecimal close;

    /**
     * 变化
     */
    @Column(name = "stock_change")
    private BigDecimal change;

    /**
     * 变化
     */
    @Column(name = "stock_change_pct")
    private String changePct;

    /**
     * 成交量
     */
    private BigDecimal volume;

    /**
     * 成交额
     */
    private BigDecimal amount;

    /**
     * 振幅
     */
    private BigDecimal swing;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;

    private Long ts;
}