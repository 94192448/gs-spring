package com.example.demo.service;

import com.example.demo.Constans;
import com.example.demo.po.TerminalSessionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-05
 */
@Service
@Slf4j
public class TerminalSessionService {

    private static Map<String, TerminalSessionWrapper> terminalSessionsCache = new ConcurrentHashMap<>();

    public Set<String> apiSessionIDs(){
        return terminalSessionsCache.keySet();
    }

    public void initSession(TerminalSessionWrapper terminalSessionWrapper) {

        terminalSessionWrapper.setTerminalSessionId(getTerminalSessionId(terminalSessionWrapper.getWebSocketSession()));

        terminalSessionsCache.put(getTerminalSessionId(terminalSessionWrapper.getWebSocketSession()), terminalSessionWrapper);
    }

    /**
     * 每打开一个终端分配唯一sessionID
     * @param webSocketSession
     * @return
     */
    public String getTerminalSessionId(WebSocketSession webSocketSession){

        return String.valueOf(webSocketSession.getAttributes().get(Constans.TERMINAL_SESSION_KEY));
    }

    public TerminalSessionWrapper getTerminalSession(WebSocketSession webSocketSession){

        return getTerminalSession(getTerminalSessionId(webSocketSession));
    }

    public TerminalSessionWrapper getTerminalSession(String terminalSessionId){

        return terminalSessionsCache.get(terminalSessionId);
    }

    public void closeTerminalSession(WebSocketSession webSocketSession) {

        TerminalSessionWrapper sessionWrapper = getTerminalSession(webSocketSession);

        log.debug("Starting close terminalSessionID:{}",sessionWrapper.getTerminalSessionId());

        if (sessionWrapper != null) {

            if (sessionWrapper.getChannel() != null) {

                sessionWrapper.getChannel().disconnect();

            }
            terminalSessionsCache.remove(getTerminalSessionId(webSocketSession));
        }
    }
}
