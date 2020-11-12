package com.example.wssh.config;

import com.example.wssh.web.TerminalWebSocketHandler;
import com.example.wssh.WebSocketTerminalHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configure a WebSocketHandler at the specified URL paths.
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@Configuration
@EnableWebSocket
public class WsshWebSocketConfig implements WebSocketConfigurer {
    @Autowired
    TerminalWebSocketHandler webSSHWebSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

        webSocketHandlerRegistry.addHandler(webSSHWebSocketHandler, "/webssh")
                .addInterceptors(new WebSocketTerminalHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

}
