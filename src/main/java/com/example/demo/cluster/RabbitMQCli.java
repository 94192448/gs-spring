package com.example.demo.cluster;

import com.example.demo.CliExecutor;
import com.example.demo.CommandExec;
import com.example.demo.cluster.po.Cluster;
import com.example.demo.cluster.po.Node;
import org.apache.commons.cli.CommandLine;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-09
 */
public class RabbitMQCli extends CliExecutor {

    @Override
    public void execute(CommandLine line) {

        String cmd = "rabbitmqctl cluster_status";

        String rs = "Cluster status of node rabbit@rabbit3 ...\n" +
                "[{nodes,[{disc,[rabbit@node7]},{ram,[rabbit@rabbit3,rabbit@rabbit2]}]},\n" +
                " {running_nodes,[rabbit@rabbit2,rabbit@node7,rabbit@rabbit3]},\n" +
                " {cluster_name,<<\"rabbit@node7\">>},\n" +
                " {partitions,[]},\n" +
                " {alarms,[{rabbit@rabbit2,[]},{rabbit@node7,[]},{rabbit@rabbit3,[]}]}]";
//        String rs = CommandExecutor.execCmd(cmd);

        String allNodes = CommandExec.extractSubString(rs, "\\[\\{nodes,\\[(.*?),\n");

        String runningNodes = CommandExec.extractSubString(rs, "running_nodes,\\[(.*?)\\]}");
        ;

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
