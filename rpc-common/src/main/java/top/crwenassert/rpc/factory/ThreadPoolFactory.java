package top.crwenassert.rpc.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * ClassName: ThreadPoolFactory
 * Description: 创建 ThreadPool（线程池）工具类
 * date: 2020/12/17 15:01
 *
 * @author crwen
 * @create 2020-12-17-15:01
 * @since JDK 1.8
 */
@Slf4j
public class ThreadPoolFactory {

    // 线程池参数
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private static Map<String, ExecutorService> threadPoolMap = new ConcurrentHashMap<>();

    private ThreadPoolFactory() {}

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix) {
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean daemon) {
        ExecutorService pool = threadPoolMap.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix, daemon));
        if (pool.isShutdown() || pool.isTerminated()) {
            threadPoolMap.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix, daemon);
            threadPoolMap.put(threadNamePrefix, pool);
        }
        return pool;
    }

    public static ExecutorService createThreadPool(String threadNamePrefix, Boolean daemon) {
        // 使用有界队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     *  创建 ThreadFactory。
     *  如果 threadNamePrefix 不为空，则使用自建的 ThreadFactory，否则使用 defaultThreadFactory
     * @param threadNamePrefix
     * @param daemon
     * @return ThreadFactory
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }

        return Executors.defaultThreadFactory();
    }

    /**
     * 关闭线程池
     *
     */
    public static void shutDownAll() {
        log.info("关闭所有线程池...");
        threadPoolMap.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            log.info("关闭线程池 [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                log.error("关闭线程池失败！");
                executorService.shutdownNow();
            }
        });
    }
}
