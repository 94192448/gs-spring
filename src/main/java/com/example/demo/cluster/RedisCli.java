package com.example.demo.cluster;

import com.example.demo.CliExecutor;
import com.example.demo.CommandExec;
import com.example.demo.cluster.po.Cluster;
import com.example.demo.cluster.po.Node;
import org.apache.commons.cli.CommandLine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-08
 */
public class RedisCli extends CliExecutor {

    @Override
    public void execute(CommandLine line) {

        String cli = line.getOptionValue("cli", "./redis-cli");
        String host = line.getOptionValue("h", "127.0.0.1");
        String port = line.getOptionValue("p", "6379");

        handleOutput(getCluster(cli, host, port));

    }

    @Override
    public String getClusterType() {
        return "redis";
    }

    Cluster getCluster(String cli, String host, String port) {

        String cmdLine = cli + " --cluster check " + host + ":" + port;

        String rs = CommandExec.execCmd(cmdLine);


        if (!rs.contains("Performing Cluster Check")) {
            System.out.println(rs);
            System.exit(1);
        }

        Cluster cluster = newCluster();

        if (!rs.contains("[OK] All 16384 slots covered")) {
            cluster.setHealth("DOWN");
        }

        cluster.setNodes(getClusterNodes(cli, host, port));

        return cluster;
    }

    List<Node> getClusterNodes(String cli, String host, String port) {

        String cmdLine = cli + " -h " + host + " -p " + port + " cluster nodes";

        String rs = CommandExec.execCmd(cmdLine);


        List<Node> nodes = new ArrayList<Node>();
        for (String l : rs.split("\n")) {
            String[] ls = l.split(" ");
            if (ls.length < 3) {
                continue;
            }

            Node node = newNode();

            node.setId(ls[0]);
            node.setIp(ls[1].split("@")[0]);

            if (ls[2].contains("master")) {
                node.setRole("master");
            } else {
                node.setRole("slave");
                node.setPid(ls[3]);
            }

            if (l.contains("fail")) {
                node.setHealth("DOWN");
            }

            nodes.add(node);

        }
        return nodes;

    }
}
