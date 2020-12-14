package top.crwenassert.rpc.service;

import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.registry.DefaultServiceRegistry;
import top.crwenassert.rpc.registry.ServiceRegistry;
import top.crwenassert.rpc.socket.server.SocketServer;

/**
 * ClassName: TestServer
 * Description:
 * date: 2020/11/13 13:41
 *
 * @author crwen
 * @create 2020-11-13-13:41
 * @since JDK 1.8
 */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer rpcServer = new SocketServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
