package com.example.demo.cluster;

import com.example.demo.AbstractCLIExecutor;
import com.example.demo.CommandExec;
import com.example.demo.cluster.po.Cluster;
import com.example.demo.cluster.po.Node;
import org.apache.commons.cli.CommandLine;

/**
 * fdfs_monitor /etc/fdfs/client.conf 中配置 tracker_server=192.168.251.156:22122 即可远程监控
 * @author yangzq80@gmail.com
 * @date 2020-12-10
 */
public class FastDFSCLI extends AbstractCLIExecutor {
    @Override
    public void execute(CommandLine line) {

        String cmd = line.getOptionValue("cli","fdfs_monitor /etc/fdfs/client.conf");

        String rs = CommandExec.execCmd(cmd);

        Cluster cluster = newCluster();

        String group = "";

        int i =0;
        String[] rss = rs.split("\n");
        for(String l:rss){
            l=l.replaceAll("\t","");
            i++;
            if (l.length()==0){
                continue;
            }
            if (l.contains("tracker server is")){
                Node node = newNode();
                node.setId(l.replace("tracker server is ",""));
                node.setIp(node.getId());
                node.setRole("tracker");

                addNode(cluster,node);
            }

            if (l.contains("group name = ")){
                group = l.replace("group name = ","");
            }

            if (l.contains("Storage")){
                Node node = newNode();
                node.setId((group+"-"+l.replace(":","").replace(" ","")).toLowerCase());
                node.setRole("storage");
                node.setIp(rss[i].split("=")[1]);
                node.setDes(group);

                if (!rss[i+1].contains("ACTIVE")){
                    node.setHealth("DOWN");
                }

                addNode(cluster,node);
            }
        }

        handleOutput(cluster);
    }

    @Override
    public String getClusterType() {
        return "fastdfs";
    }
}
