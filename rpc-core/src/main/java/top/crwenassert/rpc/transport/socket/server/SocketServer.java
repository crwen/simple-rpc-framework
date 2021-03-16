package top.crwenassert.rpc.transport.socket.server;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.factory.ThreadPoolFactory;
import top.crwenassert.rpc.transport.handler.RequestHandler;
import top.crwenassert.rpc.hook.ShutdownHook;
import top.crwenassert.rpc.provide.ServiceProvider;
import top.crwenassert.rpc.provide.ServiceProviderImpl;
import top.crwenassert.rpc.registry.NacosServiceRegistry;
import top.crwenassert.rpc.registry.ServiceRegistry;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.transport.AbstractRPCServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * ClassName: RPCServer
 * Description: Socket方式远程方法调用的服务提供者（服务端）
 * date: 2020/11/13 13:45
 *
 * @author crwen
 * @create 2020-11-13-13:45
 * @since JDK 1.8
 */
@Slf4j
public class SocketServer extends AbstractRPCServer {

    private final String host;
    private final int port;
    private ExecutorService threadPool;
    private final RequestHandler requestHandler = new RequestHandler();
    private final CommonSerializer serializer;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;


    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    /**
     *  服务端主动注册服务
     */
    public void start() {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCErrorEnum.SERIALIZER_NOT_FOUND);
        }
        try(ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务器启动...");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接： {}:{}",socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serviceRegistry, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动发生错误: ", e);
        }
    }



}
