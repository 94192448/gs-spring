package com.example.wssh.web;

import com.example.wssh.po.BatchCommandVM;
import com.example.wssh.po.TerminalSessionMetaVM;
import com.example.wssh.po.TerminalVM;
import com.example.wssh.service.SSHExecutorAPI;
import com.example.wssh.service.TerminalExecutor;
import com.example.wssh.service.TerminalSessionService;
import com.example.wssh.service.jsch.SshClientShell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    SSHExecutorAPI sshExecutorAPI;

    @Autowired
    SshClientShell sshClientShell;

    @RequestMapping("/authentication")
    public String authentication(@RequestBody TerminalVM terminalVM) {

        return sshExecutorAPI.authentication(terminalVM);
    }

    @RequestMapping("/session")
    public List<TerminalSessionMetaVM> session() {

        return terminalSessionService.apiSession();
    }

    @RequestMapping("/session/ids")
    public Set<String> sessionIds() {

        return terminalSessionService.apiSessionIDs();
    }

    /**
     * 批量执行并终端显示结果
     * @param vm
     * @throws IOException
     */
    @RequestMapping("/command")
    public void executeCommand(@RequestBody BatchCommandVM vm) throws IOException {

        for (String id : vm.getSessionIds()) {

            sshExecutorAPI.executeSshShellCommand(id, vm.getCommand() + "\r");
        }

    }

    /**
     * 批量执行并同步返回结果
     * @param vm
     * @throws IOException
     */
    @RequestMapping("/command/exec")
    public Map<String,String> executeCommandExec(@RequestBody BatchCommandVM vm) throws IOException {

        Map<String,String> rs = new HashMap();

        for (String id : vm.getSessionIds()) {

            rs.put(id,sshExecutorAPI.executeSshExecCommand(id, vm.getCommand()));

        }
        return rs;
    }
}
