package com.example.demo;

import com.example.demo.cluster.*;
import org.apache.commons.cli.*;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-07
 */
public class ClusterCLI {

    public static void main(String[] args) {

        simpleTest(args);
    }

    public static void simpleTest(String[] args) {

        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        Options options = new Options();
        options.addOption("help", "help", false, "help command list .");

        options.addOption("redis", "redis", false, "--redis [--h=127.0.0.1 --p=3379 --cli=./redis-cli]");
        options.addOption("rabbit", "rabbitmq", false, "--rabbitmq [--h=rabbit@host]");
        options.addOption("es", "elasticsearch", false, "--elasticsearch");
        options.addOption("fastdfs", "fastdfs", false, "--fastdfs [--cli=fdfs_monitor /etc/fdfs/client.conf]");
        options.addOption("zk", "zookeeper", false, "--zookeeper [--cli=zkServer.sh status]");

        options.addOption(Option.builder().longOpt("h").hasArg().desc("hostname").build());
        options.addOption(Option.builder().longOpt("p").hasArg().desc("port").build());
        options.addOption(Option.builder().longOpt("cli").hasArg().desc("command line").build());

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("help")) {
                new HelpFormatter().printHelp("help", options);
            }

            if (line.hasOption("redis")) {

                new RedisCLI().execute(line);
            }

            if (line.hasOption("rabbitmq")) {

                new RabbitMQCLI().execute(line);
            }

            if (line.hasOption("elasticsearch")) {
                new ElasticsearchCLI().execute(line);
            }
            if (line.hasOption("fastdfs")) {

                new FastDFSCLI().execute(line);
            }
            if (line.hasOption("zk")) {

                new ZookeeperCLI().execute(line);
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}
