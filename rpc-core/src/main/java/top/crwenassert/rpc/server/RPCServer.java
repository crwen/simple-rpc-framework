package top.crwenassert.rpc.server;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * ClassName: RPCServer
 * Description:
 * date: 2020/11/13 13:45
 *
 * @author crwen
 * @create 2020-11-13-13:45
 * @since JDK 1.8
 */
@Slf4j
public class RPCServer {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;


    public RPCServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;

        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     *  服务端主动注册服务
     * @param port
     */
    public void start(int port) {
        try(ServerSocket server = new ServerSocket(port);) {
            log.info("服务器启动...");
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("消费者连接： {}:{}",socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动发生错误: ", e);
        }
    }
}
