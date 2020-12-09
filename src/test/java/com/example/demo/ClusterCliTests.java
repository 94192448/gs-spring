package com.example.demo;

import org.junit.Test;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-07
 */
public class ClusterCliTests {

    // 测试带有 –h 参数的代码功能
    @Test
    public void testHelp() {
        String args[] = {"--help"};
        ClusterCli.simpleTest(args);
    }

    @Test
    public void testRedisCli() {
        String args[] = {"--type=redis","--cli=docker exec -i redis redis-cli","--p=7001","--h=172.16.20.223"};
        ClusterCli.simpleTest(args);
    }

    @Test
    public void testRabbit(){
        String args[] = {"--type=rabbitmq"};
        ClusterCli.simpleTest(args);
    }
}
