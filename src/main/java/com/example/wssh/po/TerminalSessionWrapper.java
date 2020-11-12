package com.example.wssh.po;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-05
 */
@Data
public class TerminalSessionWrapper {
    private String terminalSessionId;

    private WebSocketSession webSocketSession;

    private Session jschSession;

    private Channel channel;

    private ChannelSftp channelSftp;

    //Extends
    TerminalSessionMetaVM metaVM;

}
