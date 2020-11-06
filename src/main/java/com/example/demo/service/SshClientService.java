package com.example.demo.service;

import com.example.demo.po.TerminalSessionWrapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@Service
@Slf4j
public class SshClientService {

    public Session openConnection(String host, int port, String username, String password,int connectTimeout) throws JSchException {

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        //获取jsch的会话
        Session session = new JSch().getSession(username, host, port);

        session.setConfig(config);

        session.setPassword(password);

        session.connect(30000);

        return session;
    }

    public void send(TerminalSessionWrapper sessionWrapper, String command) throws IOException {
        if (sessionWrapper.getChannel() != null) {
            OutputStream outputStream = sessionWrapper.getChannel().getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }


    public void close(Session session, Channel channel, InputStream inputStream) throws IOException {

        session.disconnect();

        channel.disconnect();

        if (inputStream != null) {
            inputStream.close();
        }
    }
}
