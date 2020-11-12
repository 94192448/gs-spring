package com.example.wssh.service;

import com.example.wssh.ConfigConstants;
import com.example.wssh.po.TerminalSessionWrapper;
import com.example.wssh.po.TerminalVM;
import com.example.wssh.service.jsch.SshClientFileTransfer;
import com.example.wssh.service.jsch.SshClientService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebSocket实现ssh终端操作.使用{@link SshClientService}实现ssh操作
 * 与{@link TerminalSessionService}实现终端session管理
 *
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@Service
@Slf4j
public class TerminalExecutor {

    @Autowired
    SshClientService sshClientService;

    @Autowired
    SshClientFileTransfer sshClientFileTransfer;

    @Autowired
    TerminalSessionService terminalSessionService;

    ExecutorService executorService = Executors.newCachedThreadPool();

    public void initTerminal(WebSocketSession webSocketSession) {

        TerminalSessionWrapper sessionWrapper = new TerminalSessionWrapper();

        sessionWrapper.setWebSocketSession(webSocketSession);

        terminalSessionService.initSession(sessionWrapper);

    }

    private void outputMessage(WebSocketSession webSocketSession, byte[] buffer) throws IOException {

        webSocketSession.sendMessage(new TextMessage(buffer));

    }

    public void openSshConnection(TerminalVM terminalVM, WebSocketSession webSocketSession) throws Exception {

        TerminalSessionWrapper sessionWrapper = terminalSessionService.getTerminalSession(webSocketSession);

        //更新session元数据信息
        terminalSessionService.recordMeta(terminalVM, webSocketSession);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    Session session = sshClientService.openConnection(terminalVM.getHost(), terminalVM.getPort(),
                            terminalVM.getUsername(), terminalVM.getPassword(), terminalVM.getConnectTimeout());

                    //Open the channel of shell
                    Channel sshChannel = sshClientService.openShellChannel(session);

                    sessionWrapper.setChannel(sshChannel);

                    sessionWrapper.setJschSession(session);

                    //For test
                    sshClientService.send(sessionWrapper, "\r");

                    inputStream = sshChannel.getInputStream();
                    byte[] buffer = new byte[1024];
                    int i = 0;
                    //线程阻塞等待数据发生
                    while ((i = inputStream.read(buffer)) != -1) {
                        outputMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
                    }

                } catch (JSchException | IOException e) {

                    log.error("Terminal connection error:{}", e.getMessage());

                    terminalSessionService.closeTerminalSession(webSocketSession);

                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
            }
        });
    }

    public void executeSshCommand(TerminalVM terminalVM, WebSocketSession webSocketSession) throws IOException {

        sshClientService.send(terminalSessionService.getTerminalSession(webSocketSession), terminalVM.getCommand());
    }

    public void closeTerminal(WebSocketSession webSocketSession) {
        terminalSessionService.closeTerminalSession(webSocketSession);
    }

    public void executeSshCommand(String sessionId, String cmd) {

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
            sshClientService.send(sessionWrapper, cmd);
        } catch (Exception e) {
            log.error("Executing [{}] error: {}", cmd, e.getMessage());
        }
    }
}
