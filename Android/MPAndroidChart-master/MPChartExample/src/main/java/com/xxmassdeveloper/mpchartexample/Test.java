package com.xxmassdeveloper.mpchartexample;

import com.xxmassdeveloper.mpchartexample.tool.DateFormatTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/26
 */
public class Test {

    public static void main(String [] args){


        System.out.println(DateFormatTool.getCurrentTime());
        try {
            System.out.println(DateFormatTool.getUTCDateForAMPMFormat(String.valueOf(System.currentTimeMillis())));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
