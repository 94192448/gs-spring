package com.example.demo.cluster;

import com.example.demo.AbstractCLIExecutor;
import com.example.demo.CommandExec;
import com.example.demo.cluster.po.Cluster;
import com.example.demo.cluster.po.Node;
import org.apache.commons.cli.CommandLine;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-10
 */
public class ZookeeperCLI extends AbstractCLIExecutor {

    @Override
    public void execute(CommandLine line) {

        String cmd = line.getOptionValue("cli","echo srvr | nc hostname 2181");


        String rsNodes = CommandExec.execCmd("cat /conf/zoo.cfg");

        Cluster cluster = newCluster();

        for(String l:rsNodes.split("\n")){
            if (l.startsWith("server.")){
                Node node = newNode();
                String host = l.split("=")[1].split(":")[0];
                node.setIp(host);
                String rsNode = CommandExec.execCmd(cmd.replace("hostname",host));

                if (rsNode.contains("This ZooKeeper instance is not currently serving requests")){
                    cluster.setHealth("DOWN");
                    break;
                }

                //Set node role.
                for(String ln:rsNode.split("\n")){
                    if (ln.startsWith("Mode:")){
                        if (ln.contains("leader")){
                            node.setRole("leader");
                        }else if(ln.contains("observer")){
                            node.setRole("observer");
                        }else {
                            node.setRole("follower");
                        }
                        break;
                    }
                }

                node.setId(host+"-"+node.getRole());

                addNode(cluster,node);
            }
        }

        handleOutput(cluster);

    }

    @Override
    public String getClusterType() {
        return "zooKeeper";
    }
}
