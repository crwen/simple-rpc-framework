package top.crwenassert.rpc.service;

import top.crwenassert.rpc.netty.server.NettyServer;
import top.crwenassert.rpc.registry.DefaultServiceRegistry;

/**
 * ClassName: TestNettyServer
 * Description:
 * date: 2020/12/13 18:05
 *
 * @author crwen
 * @create 2020-12-13-18:05
 * @since JDK 1.8
 */
public class TestNettyServer {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
