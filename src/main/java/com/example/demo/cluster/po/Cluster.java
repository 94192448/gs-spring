package com.example.demo.cluster.po;

import java.util.List;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-08
 */
public class Cluster {

    private String name;

    private String health="UP";

    private List<Node> nodes;

    private String des;

    public Cluster(String name, String health,  List<Node> nodes) {
        this.name = name;
        this.health = health;
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public  List<Node> getNodes() {
        return nodes;
    }

    public void setNodes( List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

