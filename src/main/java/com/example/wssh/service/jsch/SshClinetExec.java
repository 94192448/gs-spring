package com.example.wssh.service.jsch;

import com.example.wssh.po.TerminalSessionWrapper;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-13
 */
@Service
@Slf4j
public class SshClinetExec {

    public String execCommand(TerminalSessionWrapper wrapper, String cmd) {

        Session session = wrapper.getJschSession();

        ChannelExec channel = null;

        String response = "";
        try {
            channel = (ChannelExec) session.openChannel("exec");

            channel.setCommand(cmd);

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorResponseStream = new ByteArrayOutputStream();

            channel.setOutputStream(responseStream);
            channel.setErrStream(errorResponseStream);

            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            log.debug("Successed connected exec channel,start execute [{}]...",cmd);

            String errorResponse = new String(errorResponseStream.toByteArray());

            response = new String(responseStream.toByteArray());

            if (!errorResponse.isEmpty()) {
                return errorResponse;
            }
        } catch (JSchException | InterruptedException e) {
            log.error("ChannelExec executor error:{}", e.getMessage());
        } finally {
            if (channel != null) {

                channel.disconnect();
            }
        }

        return response;
    }
}
