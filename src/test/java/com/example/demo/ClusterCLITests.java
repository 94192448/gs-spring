package com.example.demo;

import org.junit.Test;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-07
 */
public class ClusterCLITests {

    // 测试带有 –h 参数的代码功能
    @Test
    public void testHelp() {
        String args[] = {"--help"};
        ClusterCLI.simpleTest(args);
    }

    @Test
    public void testRedisCli() {
        String args[] = {"--redis","--cli=docker exec -i redis redis-cli","--p=7001","--h=172.16.20.223"};
        ClusterCLI.simpleTest(args);
    }

    @Test
    public void testRabbit(){
        String args[] = {"--rabbitmq"};
        ClusterCLI.simpleTest(args);
    }

    @Test
    public void testEs(){
        String args[] = {"--elasticsearch","--h=192.168.251.156","--p=9203"};
        ClusterCLI.simpleTest(args);
    }

    @Test
    public void testFastDFS(){
        String args[] = {"--fastdfs"};
        ClusterCLI.simpleTest(args);
    }

    @Test
    public void testZookeeper(){
        String args[] = {"--zookeeper"};
        ClusterCLI.simpleTest(args);
    }
}
