package top.crwenassert.rpc.service;

import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.transport.netty.server.NettyServer;

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
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.JSON_SERIALIZER);
        server.publishService(helloService, HelloService.class);
    }
}
