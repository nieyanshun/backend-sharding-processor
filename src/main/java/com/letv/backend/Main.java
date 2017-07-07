package com.letv.backend;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.letv.backend.service.ShardingService;

public class Main {

    private static Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        final ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        LOG.info("init.........");
        context.start();
        testShard(context);
        context.stop();
    }

    public static void testShard(ApplicationContext context) {
        ShardingService service = (ShardingService) context.getBean("shardingService");
        service.start();;
    }

}
