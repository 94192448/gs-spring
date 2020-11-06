package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-05
 */
@Slf4j
public class WebSocketTerminalHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {

            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;

            String uuid = UUID.randomUUID().toString().replace("-","");

            attributes.put(Constans.TERMINAL_SESSION_KEY, uuid);

            log.debug("Initializing terminal sessionId:{}",uuid);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
