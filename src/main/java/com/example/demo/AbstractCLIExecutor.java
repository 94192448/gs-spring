package com.example.demo;

import cn.hutool.json.JSONUtil;
import com.example.demo.cluster.po.Cluster;
import com.example.demo.cluster.po.Node;
import org.apache.commons.cli.CommandLine;

import java.util.ArrayList;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-08
 */
public abstract class AbstractCLIExecutor {

    public abstract void execute(CommandLine line);

    public abstract String getClusterType();

    public Cluster newCluster(){
        return new Cluster(getClusterType(),"UP", new ArrayList<Node>());
    }

    public Node newNode(){
        return new Node();
    }

    public void addNode(Cluster cluster,Node node){
        cluster.getNodes().add(node);
    }


    public void handleOutput(Cluster cluster){

        System.out.println(JSONUtil.toJsonStr(cluster));
    }

}
