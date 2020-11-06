package com.example.demo.po;

import lombok.Data;

/**
 * View Model object for representing a user's credentials
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@Data
public class TerminalVM {
    String operate;
    String host;
    int port = 22;
    String username;
    String password;
    String command = "";
    /**
     * 连接超时 30s
     */
    int connectTimeout = 30000;
}
