package com.example.demo.cluster;

import com.example.demo.AbstractCLIExecutor;
import com.example.demo.CommandExec;
import com.example.demo.cluster.po.Cluster;
import com.example.demo.cluster.po.Node;
import org.apache.commons.cli.CommandLine;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-09
 */
public class RabbitMQCLI extends AbstractCLIExecutor {

    @Override
    public void execute(CommandLine line) {

        String cmd = "rabbitmqctl cluster_status";

        if (line.hasOption("h")){
            cmd =  "rabbitmqctl -n "+line.getOptionValue("h")+" cluster_status";
        }

        String rs = CommandExec.execCmd(cmd);

        String allNodes = CommandExec.extractSubString(rs, "\\[\\{nodes,\\[(.*?),\n");

        if (allNodes.length()==0){
            System.out.println("error:"+rs);
            return;
        }

        String runningNodes = CommandExec.extractSubString(rs, "running_nodes,\\[(.*?)\\]}");

        String discNodes = CommandExec.extractSubString(allNodes, "disc,\\[(.*?)\\]}");

        String ramNodes = CommandExec.extractSubString(allNodes, "ram,\\[(.*?)\\]}");

        Cluster cluster = newCluster();

        for (String n : discNodes.split(",")) {
            Node node = newNode();
            node.setId(n);
            node.setRole("disc");
            node.setIp(n.split("@")[1]);

            if (!runningNodes.contains(n)) {
                node.setHealth("DOWN");
            }

            addNode(cluster, node);
        }

        for (String n : ramNodes.split(",")) {
            Node node = newNode();
            node.setId(n);
            node.setRole("ram");
            node.setIp(n.split("@")[1]);

            if (!runningNodes.contains(n)) {
                node.setHealth("DOWN");
            }

            addNode(cluster, node);
        }

        handleOutput(cluster);

    }

    @Override
    public String getClusterType() {
        return "rabbitmq";
    }
}
