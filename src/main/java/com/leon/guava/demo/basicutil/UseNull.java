package com.leon.guava.demo.basicutil;

import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * @author: chenliang
 * @Date: 2020年12月10
 */
public class UseNull {

    public static void main(String[] args) {
        UseNull useNull = new UseNull();
        useNull.useNull();
    }

    //null 的一些使用场景
    int score;
    Integer score2;

    public void useNull() {
        //1. null标识值不存在
        System.out.println("score:" + score + "\tscore2:" + score2);
        //2. 集合框架接收Null值
        List<Object> list = new ArrayList<Object>();
        list.add(null);

        List<Object> list1 = new LinkedList<Object>();
        list1.add(null);
    }
}
