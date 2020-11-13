package top.crwenassert.rpc.server;

import lombok.extern.slf4j.Slf4j;

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
    private ExecutorService threadPool;

    public RPCServer() {
        // 线程池参数
        int corePoolSize = 5;
        int maxmumPoolSize = 50;
        long keepAliveTime = 10;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maxmumPoolSize,
                keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     *  服务端主动注册服务
     * @param service
     * @param port
     */
    public void register(Object service, int port) {
        try(ServerSocket server = new ServerSocket(port);) {
            log.info("server starts...");
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected. ip: " + socket.getRemoteSocketAddress());
                threadPool.execute(new RequestHandler(socket, service));
            }
        } catch (IOException e) {
            log.error("occur IOException: ", e);
        }
    }
}
