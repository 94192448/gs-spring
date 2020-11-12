package com.example.wssh;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import org.junit.Test;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-03
 */
public class SSHJTests extends SSHTestBase{


    private static final Console con = System.console();
    /** This examples demonstrates how a remote command can be executed. */
    @Test
    public void TestSSHJ() throws Exception{
        final SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.connect(hostname);

        Session session = null;
        try {
            //ssh.authPublickey(System.getProperty("user.name"));
            ssh.authPassword(username,password);
            session = ssh.startSession();
            final Session.Command cmd = session.exec("ping -c 1 baidu.com");
            con.writer().print(IOUtils.readFully(cmd.getInputStream()).toString());
            cmd.join(5, TimeUnit.SECONDS);
            con.writer().print("\n** exit status: " + cmd.getExitStatus());
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (IOException e) {
                // Do Nothing
            }

            ssh.disconnect();
        }
    }

}
