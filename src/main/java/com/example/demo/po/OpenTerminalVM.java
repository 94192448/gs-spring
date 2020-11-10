package com.example.demo.po;

import lombok.Data;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-10
 */
@Data
public class OpenTerminalVM extends TerminalVM{
    int cols = 97;
    int rows = 37;
    int scrollback = 800;
}
