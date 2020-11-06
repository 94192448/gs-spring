package com.example.demo.web;

import com.example.demo.po.TerminalVM;
import com.example.demo.service.TerminalExecutor;
import com.example.demo.service.TerminalSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@RestController
@RequestMapping("/wssh/api")
@Slf4j
public class TerminalAPIController {

    @Autowired
    TerminalSessionService terminalSessionService;

    @Autowired
    TerminalExecutor terminalExecutor;

    @RequestMapping("/ids")
    public Set<String> websshpage() {

        return terminalSessionService.apiSessionIDs();
    }

    @RequestMapping("/cmd")
    public void executeCommand(String sessionId,String cmd) throws IOException {

        terminalExecutor.executeSshCommand(sessionId,cmd+"\r");
    }
}
