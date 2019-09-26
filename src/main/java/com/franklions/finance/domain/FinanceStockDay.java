package com.franklions.finance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "finance_stock_day")
public class FinanceStockDay {
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
     * 公司名称
     */
    @Column(name = "company")
    private String company;

    /**
     * 所属板块
     */
    @Column(name = "block")
    private String block;

    /**
     * 机构评级
     */
    @Column(name = "stock_grade")
    private  Integer stockGrade;

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
     * 总股本
     */
    private BigDecimal zgb;

    /**
     * 成交量
     */
    private BigDecimal volume;

    /**
     * 成交额
     */
    private BigDecimal amount;

    /**
     * 总市值
     */
    @Column(name = "fix_total_share")
    private BigDecimal fixTotalShare;

    /**
     * 流通值
     */
    private BigDecimal cvs;

    /**
     * 流通股
     */
    private BigDecimal ltgb;

    /**
     * 振幅
     */
    private BigDecimal swing;

    /**
     * 换手率
     */
    private BigDecimal turnover;

    /**
     * 市净率
     */
    private BigDecimal pb;

    /**
     * 市盈率TTM
     */
    private BigDecimal pe;

    /**
     * 主力买入
     */
    @Column(name = "main_in")
    private BigDecimal mainIn;

    /**
     * 主力卖出
     */
    @Column(name = "main_out")
    private BigDecimal mainOut;

    /**
     * 散户买入
     */
    @Column(name = "retail_in")
    private BigDecimal retailIn;

    /**
     * 散户卖出
     */
    @Column(name = "retail_out")
    private BigDecimal retailOut;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;

    private Long ts;
}