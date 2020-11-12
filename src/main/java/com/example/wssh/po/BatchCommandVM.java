package com.example.wssh.po;

import lombok.Data;

import java.util.List;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-10
 */
@Data
public class BatchCommandVM {
    String command;
    List<String> sessionIds;
}
