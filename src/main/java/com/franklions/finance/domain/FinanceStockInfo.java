package com.franklions.finance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "finance_stock_info")
public class FinanceStockInfo {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

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
     * 类型sh/sz
     */
    @Column(name = "stock_type")
    private String stockType;

    /**
     * 删除标识
     */
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 时间戳
     */
    private Long ts;
}