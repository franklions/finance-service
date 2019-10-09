package com.franklions.finance.utils;

import java.math.BigDecimal;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-10-09
 * @since Jdk 1.8
 */
public class Utils {
    public static BigDecimal strToDecimal(String str) {
        if(str == null || "".equals(str)){
            return BigDecimal.ZERO;
        }

        str = str.replaceAll("手","")
                .replaceAll("万","")
                .replaceAll("亿","")
                .replaceAll("元","")
                .replaceAll("%","");

        try {
            return new BigDecimal(str);
        }catch (Exception ex){
            return BigDecimal.ZERO;
        }
    }
}
