package com.example.demo;

import com.example.demo.cluster.RabbitMQCli;
import com.example.demo.cluster.RedisCli;
import org.apache.commons.cli.*;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-07
 */
public class ClusterCli {

    public static void main(String[] args) {

        simpleTest(args);
    }

    public static void simpleTest(String[] args) {

        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        Options options = new Options();
        options.addOption("help", "help", false, "help command list .");

        options.addOption("t", "type", true, "redis,rabbitmq,zookeeper");

        options.addOption(Option.builder().longOpt("h").hasArg().desc("hostname").build());
        options.addOption(Option.builder().longOpt("p").hasArg().desc("port").build());
        options.addOption(Option.builder().longOpt("cli").hasArg().desc("command line").build());

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("help")) {
                new HelpFormatter().printHelp("help", options);
            }
            // validate that block-size has been set
            if (line.hasOption("t")) {
                // print the value of block-size
                String type = line.getOptionValue("type");

                if ("redis".equalsIgnoreCase(type)) {

                    new RedisCli().execute(line);
                }

                if ("rabbitmq".equalsIgnoreCase(type)) {

                    new RabbitMQCli().execute(line);
                }
            }else {
                System.out.println("Undefined --type");
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}
