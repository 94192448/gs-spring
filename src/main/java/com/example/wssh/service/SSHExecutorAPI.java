package com.example.wssh.service;

import com.example.wssh.ConfigConstants;
import com.example.wssh.po.TerminalSessionWrapper;
import com.example.wssh.po.TerminalVM;
import com.example.wssh.service.jsch.SshClientFileTransfer;
import com.example.wssh.service.jsch.SshClientShell;
import com.example.wssh.service.jsch.SshClinetExec;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 提供ssh操作API
 * @author yangzq80@gmail.com
 * @date 2020-11-13
 */
@Service
@Slf4j
public class SSHExecutorAPI {

    @Autowired
    SshClientShell sshClientShell;

    @Autowired
    SshClientFileTransfer sshClientFileTransfer;

    @Autowired
    SshClinetExec sshClinetExec;

    @Autowired
    TerminalSessionService terminalSessionService;

    public void executeSshShellCommand(String sessionId, String cmd) {

        TerminalSessionWrapper sessionWrapper = terminalSessionService.getTerminalSession(sessionId);

        if (sessionWrapper == null) {
            log.error("Cannot execute null sessionId:{}", sessionId);
            return;
        }

        try {
            // 文件传输使用自定义命令，传输到wssh server,远程ssh server只显示结果
            if (cmd.toLowerCase().contains(ConfigConstants.CMD_FILE_EXT)) {

                sshClientFileTransfer.sftpCopy(sessionWrapper, cmd);
                cmd = "echo scp-ext success: " + cmd;
            }

            // Shell执行
            sshClientShell.send(sessionWrapper, cmd);
        } catch (Exception e) {
            log.error("Executing [{}] error: {}", cmd, e.getMessage());
        }
    }

    public String executeSshExecCommand(String sessionId, String cmd) {

        TerminalSessionWrapper sessionWrapper = terminalSessionService.getTerminalSession(sessionId);

        if (sessionWrapper == null) {
            log.error("Cannot execute null sessionId:{}", sessionId);
            return "Invalid sessionId";
        }

        return sshClinetExec.execCommand(sessionWrapper,cmd);
    }

    public String authentication(TerminalVM terminalVM) {

        TerminalSessionWrapper sessionWrapper = new TerminalSessionWrapper();

        sessionWrapper.setTerminalSessionId(UUID.randomUUID().toString().replace("-",""));

        try {
            sessionWrapper.setJschSession(sshClientShell.openConnection(terminalVM.getHost(), terminalVM.getPort(),
                    terminalVM.getUsername(), terminalVM.getPassword(), terminalVM.getConnectTimeout()));
        } catch (JSchException e) {
            log.error("Authentication error:{}",e.getMessage());

            return "Auth fail";
        }

        terminalSessionService.initializeSession(sessionWrapper);

        //更新session元数据信息
        terminalSessionService.recordMeta(terminalVM, sessionWrapper.getTerminalSessionId());

        return sessionWrapper.getTerminalSessionId();
    }
}
