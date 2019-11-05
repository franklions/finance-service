package com.franklions.finance.service.pipeline;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.franklions.finance.domain.FinanceStockDay;
import com.franklions.finance.domain.FinanceStockGrade;
import com.franklions.finance.service.FinanceStockDayService;
import com.franklions.finance.service.FinanceStockGradeService;
import com.franklions.finance.service.FinanceStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.util.List;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-25
 * @since Jdk 1.8
 */
public class FinanceStockGradePipeline implements Pipeline {
    private static final Logger logger = LoggerFactory.getLogger(FinanceStockGradePipeline.class);

    private ObjectMapper mapper;
    private FinanceStockGradeService gradeService;

    public FinanceStockGradePipeline(ObjectMapper mapper, FinanceStockGradeService gradeService) {
        this.mapper = mapper;
        this.gradeService = gradeService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if(resultItems != null || resultItems.getAll().size() > 0){

            String dayData = resultItems.get("grades");
            System.out.println(dayData);
            if (dayData == null || dayData.length() < 1) {
                return;
            }

            try {
                List<FinanceStockGrade> dataList = mapper.readValue(dayData, new TypeReference<List<FinanceStockGrade>>() {});
                if(dataList != null && dataList.size() > 0) {
                    gradeService.batchSave(dataList);
                }
            } catch (IOException e) {
                logger.error("保存股票评级数据到数据库时发生异常：" + dayData, e);
            }

        }
    }
}
