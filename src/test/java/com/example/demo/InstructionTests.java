package com.example.demo;

import com.example.demo.instruction.Instruction;
import org.junit.Test;

/**
 * @author yangzq80@gmail.com
 * @date 2021-01-07
 */
public class InstructionTests {
    @Test
    public void testAdd(){
        new Instruction().tt(11);
        new Instruction().tt(9);
    }
}
