package com.example.wssh.service.jsch;

import com.example.wssh.ConfigConstants;
import com.example.wssh.po.TerminalSessionWrapper;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-11
 */
@Service
@Slf4j
public class SshClientFileTransfer {
    public void sftpCopy(TerminalSessionWrapper wrapper, String cmd) throws JSchException, SftpException {

        //scp-ext-get test ./localtest
        String[] cmds = cmd.split(" ");

        String source = cmds[1];
        String destination = cmds[2].replace("\r","");

        Session session = wrapper.getJschSession();

        ChannelSftp channelSftp = wrapper.getChannelSftp();

        if (channelSftp == null){
            Channel channel = session.openChannel("sftp");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();

            log.debug("sftp channel connected....");
            channelSftp = (ChannelSftp) channel;

            wrapper.setChannelSftp(channelSftp);
        }

        if (cmds[0].equalsIgnoreCase(ConfigConstants.CMD_FILE_EXT_GET)) {
            channelSftp.get(source, destination);

        } else {
            channelSftp.put(source, destination);
        }
    }

}
