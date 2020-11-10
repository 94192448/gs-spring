package com.example.demo.web;

import com.example.demo.po.BatchCommandVM;
import com.example.demo.po.TerminalSessionMetaVM;
import com.example.demo.service.TerminalExecutor;
import com.example.demo.service.TerminalSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
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

    @RequestMapping("/session")
    public List<TerminalSessionMetaVM> session() {

        return terminalSessionService.apiSession();
    }

    @RequestMapping("/session/ids")
    public Set<String> sessionIds() {

        return terminalSessionService.apiSessionIDs();
    }

    @RequestMapping("/command")
    public void executeCommand(@RequestBody BatchCommandVM vm) throws IOException {

        for (String id : vm.getSessionIds()) {

            terminalExecutor.executeSshCommand(id, vm.getCommand() + "\r");
        }

    }
}
