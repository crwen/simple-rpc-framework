package top.crwenassert.rpc.service;

import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.server.RPCServer;

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
        RPCServer rpcServer = new RPCServer();
        rpcServer.register(helloService, 9000);
    }
}
