package com.newland.csc.util;

import java.util.Random;

public class Batch {

    public static String BatchId(){
        Random random = new Random();//指定种子数字
        return System.currentTimeMillis()+random.nextInt(1000)+"";
    }
}
