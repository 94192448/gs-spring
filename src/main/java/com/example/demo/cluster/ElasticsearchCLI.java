package com.example.demo.cluster;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.AbstractCLIExecutor;
import com.example.demo.cluster.po.Cluster;
import com.example.demo.cluster.po.Node;
import org.apache.commons.cli.CommandLine;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-10
 */
public class ElasticsearchCLI extends AbstractCLIExecutor {
    @Override
    public void execute(CommandLine line) {
        String host = line.getOptionValue("h", "127.0.0.1");

        String port = line.getOptionValue("p", "9200");

        String urlCluster = host + ":" + port + "/_cluster/health?pretty";

        Cluster cluster = newCluster();

        JSONObject o = JSONUtil.parseObj(HttpUtil.get(urlCluster));
        if ("red".equalsIgnoreCase(o.getStr("status"))) {
            cluster.setHealth("DOWN");
        }

        //nodes
        String urlNode = "http://" + host + ":" + port + "/_cat/nodes?v";

        String rs = HttpUtil.get(urlNode);

        cluster.setDes("status:"+o.getStr("status"));

        for (String l : rs.split("\n")) {

//            ip         heap.percent ram.percent cpu load_1m load_5m load_15m node.role master name
//            172.18.0.2           47          92   0    0.00    0.03     0.05 mdi       -      node2
            String[] ls = l.split(" \\s+");

            if (ls.length > 8) {
                Node node = newNode();
                node.setId(ls[0]);

                if ("*".equals(ls[7])) {
                    node.setRole("master");
                } else {
                    node.setRole("slave");
                }

                node.setId(ls[8]);

                node.setDes("heap:"+ls[1]+"% ram:"+ls[2]+"%");

                addNode(cluster, node);
            }

        }

        handleOutput(cluster);

    }

    @Override
    public String getClusterType() {
        return "elasticsearch";
    }

}
