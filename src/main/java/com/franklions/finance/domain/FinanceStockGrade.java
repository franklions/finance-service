package com.franklions.finance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "finance_stock_grade")
public class FinanceStockGrade {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 股票代码
     */
    @Column(name = "stock_code")
    private String stockCode;

    /**
     * 股票名称
     */
    @Column(name = "stock_name")
    private String stockName;

    /**
     * 目标价
     */
    private BigDecimal target;

    /**
     * 评级
     */
    @Column(name = "stock_grade")
    private String stockGrade;

    /**
     * 评级机构
     */
    private String company;

    /**
     * 分析师
     */
    private String analyst;

    private String block;

    /**
     * 评级日期
     */
    @Column(name = "grade_date")
    private String gradeDate;

    /**
     * 最新价
     */
    @Column(name = "stock_price")
    private BigDecimal stockPrice;

    /**
     * 行业板块
     */
    @Column(name = "stock_change")
    private BigDecimal stockChange;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;

    private Long ts;
}