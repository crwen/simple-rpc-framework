package top.crwenassert.rpc.service;

import top.crwenassert.rpc.annotation.ProviderScan;
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
@ProviderScan(basePackage = "top.crwenassert.rpc.service")
public class TestNettyServer {
    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.scanServices();
        server.start();
    }
}

