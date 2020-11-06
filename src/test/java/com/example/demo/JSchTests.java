package com.example.demo;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-03
 */

@Slf4j
public class JSchTests extends SSHTestBase {



@Test
public void givenValidCredentials_whenConnectionIsEstablished_thenServerReturnsResponse() throws Exception{
    String responseString =listFolderStructure(username,password,hostname,port,command);

    log.info(responseString);

    assertNotNull(responseString);
}

    @Test(expected = Exception.class)
    public void givenInvalidCredentials_whenConnectionAttemptIsMade_thenServerReturnsErrorResponse() throws Exception {
        String responseString =listFolderStructure("invalidUsername",password,hostname,port,command);

        log.info(responseString);

        assertNull(responseString);
    }

    public String listFolderStructure(String username, String password, String host, int port, String command) throws Exception {
        Session session = null;
        ChannelExec channel = null;
        String response = null;
        try {
            session = new JSch().getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorResponseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.setErrStream(errorResponseStream);
            channel.connect();
            while (channel.isConnected()) {
                Thread.sleep(100);
            }
            String errorResponse = new String(errorResponseStream.toByteArray());
            response = new String(responseStream.toByteArray());
            if(!errorResponse.isEmpty()) {
                throw new Exception(errorResponse);
            }
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return response;
    }
}
