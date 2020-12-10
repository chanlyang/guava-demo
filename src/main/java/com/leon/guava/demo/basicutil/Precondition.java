package com.leon.guava.demo.basicutil;

import com.google.common.base.Preconditions;

/**
 * @author: chenliang
 * @Date: 2020年12月10
 */
public class Precondition {

    public static void main(String[] args) {
        test();
    }

    public static void test(){
        Preconditions.checkArgument(false);

    }
}
