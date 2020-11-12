package com.example.wssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.junit.Test;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-11
 */
public class JschFileTransferSFTPTests extends SSHTestBase {

    @Test
    public void sftpCopy(){
        try {
//            JSch jsch = new JSch();
//
//            String privateKey = "/Users/zqy/.ssh/id_rsa";
//
//            jsch.addIdentity(privateKey);
//            System.out.println("identity added ");
//
//            Session session = jsch.getSession(username, hostname, port);
//            System.out.println("session created.");

            //
            Session session = new JSch().getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");

            // disabling StrictHostKeyChecking may help to make connection but makes it insecure
            // see http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-setconfigstricthostkeychecking-no
            //
            // java.util.Properties config = new java.util.Properties();
            // config.put("StrictHostKeyChecking", "no");
            // session.setConfig(config);

            session.connect();
            System.out.println("session connected.....");

            Channel channel = session.openChannel("sftp");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
            System.out.println("shell channel connected....");

            ChannelSftp c = (ChannelSftp) channel;

            String fileName = "test";
            //c.put(fileName, "./in/");
            c.get(fileName,"./tm");
            c.exit();
            System.out.println("done");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }
}
