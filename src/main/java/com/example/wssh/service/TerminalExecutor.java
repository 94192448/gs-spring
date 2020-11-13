package com.example.wssh.service;

import com.example.wssh.po.TerminalSessionWrapper;
import com.example.wssh.po.TerminalVM;
import com.example.wssh.service.jsch.SshClientShell;
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
 * WebSocket实现ssh终端操作.使用{@link SshClientShell}实现ssh操作
 * 与{@link TerminalSessionService}实现终端session管理
 *
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@Service
@Slf4j
public class TerminalExecutor {

    @Autowired
    SshClientShell sshClientShell;

    @Autowired
    TerminalSessionService terminalSessionService;

    ExecutorService executorService = Executors.newCachedThreadPool();

    public void initTerminal(WebSocketSession webSocketSession) {

        TerminalSessionWrapper sessionWrapper = new TerminalSessionWrapper();

        sessionWrapper.setWebSocketSession(webSocketSession);

        sessionWrapper.setTerminalSessionId(terminalSessionService.getTerminalSessionId(webSocketSession));

        terminalSessionService.initializeSession(sessionWrapper);

    }

    /**
     * ssh返回信息输出到websocket
     * @param webSocketSession
     * @param buffer
     * @throws IOException
     */
    private void outputMessage(WebSocketSession webSocketSession, byte[] buffer) throws IOException {

        webSocketSession.sendMessage(new TextMessage(buffer));

    }

    /**
     * 打开ssh连接并等待响应
     * @param terminalVM
     * @param webSocketSession
     * @throws Exception
     */
    public void openSshConnection(TerminalVM terminalVM, WebSocketSession webSocketSession) throws Exception {

        TerminalSessionWrapper sessionWrapper = terminalSessionService.getTerminalSession(webSocketSession);

        //更新session元数据信息
        terminalSessionService.recordMeta(terminalVM, sessionWrapper.getTerminalSessionId());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    Session session = sshClientShell.openConnection(terminalVM.getHost(), terminalVM.getPort(),
                            terminalVM.getUsername(), terminalVM.getPassword(), terminalVM.getConnectTimeout());

                    //Open the channel of shell
                    Channel sshChannel = sshClientShell.openShellChannel(session);

                    sessionWrapper.setChannel(sshChannel);

                    sessionWrapper.setJschSession(session);

                    //For test
                    sshClientShell.send(sessionWrapper, "\r");

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

        sshClientShell.send(terminalSessionService.getTerminalSession(webSocketSession), terminalVM.getCommand());
    }

    public void closeTerminal(WebSocketSession webSocketSession) {
        terminalSessionService.closeTerminalSession(webSocketSession);
    }

}
