package com.example.demo.web;

import com.example.demo.po.TerminalVM;
import com.example.demo.service.TerminalExecutor;
import com.example.demo.service.TerminalSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@Component
@Slf4j
public class TerminalWebSocketHandler implements WebSocketHandler {
    @Autowired
    TerminalExecutor terminalExecutor;
    @Autowired
    TerminalSessionService terminalSessionService;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.info(" WebSocket negotiation has succeeded and the WebSocket connection is opened and ready for use");
        terminalExecutor.initTerminal(webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage) {

            String buffer = ((TextMessage) webSocketMessage).getPayload();
            log.debug("Handle text WebSocket message:{}",buffer);

            TerminalVM terminalVM = new ObjectMapper().readValue(buffer, TerminalVM.class);

            if ("connect".equalsIgnoreCase(terminalVM.getOperate())){

                terminalExecutor.openSshConnection(terminalVM,webSocketSession);

            }else if ("command".equalsIgnoreCase(terminalVM.getOperate())){

                terminalExecutor.executeSshCommand(terminalVM,webSocketSession);

            }
        } else {
            log.info("Unexpected WebSocket message type:{} ",webSocketMessage);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.debug("The WebSocket connection has been closed - {}",terminalSessionService.getTerminalSessionId(webSocketSession));
        terminalExecutor.closeTerminal(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
