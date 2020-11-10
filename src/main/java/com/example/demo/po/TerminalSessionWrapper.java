package com.example.demo.po;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-05
 */
@Data
public class TerminalSessionWrapper {
    private String terminalSessionId;

    private WebSocketSession webSocketSession;

    //private JSch jSch;

    private Channel channel;

    //Extends
    TerminalSessionMetaVM metaVM;

}
