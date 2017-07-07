package com.letv.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.letv.backend.dao.ShardingItemDao;
import com.letv.backend.model.ShardingItem;

/**
 * @author nieyanshun
 *
 */
@Service
public class ShardingService {
    private static Logger LOG = Logger.getLogger(ShardingService.class);

    /**
     * 亲测性能消耗（非本段代码）：
     * 
     * 当while循环持续批处理数据，每次2000条，且批处理业务包含http请求解析和数据库batchInsert时，8G内存，4核处理器虚拟机cpu使用率不到30%,在10+%至20%
     * 浮动，甚至小于10%
     * 
     */
    private ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Autowired
    private ShardingItemDao shardingDao;

    class Worker implements Runnable {

        private int shardItem;

        Worker(int shardItem) {
            this.shardItem = shardItem;
        }

        @Override
        public void run() {
            while (true) {
                List<ShardingItem> items = shardingDao.getItems(shardItem);
                if (!CollectionUtils.isEmpty(items)) {
                    LOG.info("Shard : " + shardItem + " has data length " + items.size());
                    List<ShardingItem> memUsers = new ArrayList<>(items.size());
                    for (ShardingItem item : items) {
                        ShardingItem processedItem = doProcess(item);
                        if (null != processedItem) {
                            memUsers.add(processedItem);
                        }
                    }
                    shardingDao.batchUpdate(memUsers);
                    LOG.info("Shard : " + shardItem + " after batch process...");
                } else {
                    break;
                }
            }
        }
    }

    public void start() {
        LOG.info("Start to process membership data...");
        // 切分为10个分片
        for (int i = 0; i < 10; i++) {
            executor.execute(new Worker(i));
        }
        LOG.info("After item data submitted ...");
    }
    

    /**
     * 分片处理的业务逻辑
     * 
     * @param item
     */
    private static ShardingItem doProcess(ShardingItem item) {
        Objects.requireNonNull(item, "ShardingItem can't be null...");
        item.setState(1);
        return item;
    }
}
