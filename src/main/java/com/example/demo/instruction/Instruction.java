package com.example.demo.instruction;

/**
 * @author yangzq80@gmail.com
 * @date 2021-01-07
 */
public class Instruction {

    private int add(int a) {
        if (a > 10) {
            return 1;
        } else {
            return a + 1;
        }
    }

    public int tt(int a){
        return add(a);
    }
}
