package top.crwenassert.rpc.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
public class TestSpringNettyServer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestSpringNettyServer.class);
        NettyServer server = applicationContext.getBean(NettyServer.class);
        server.start();
    }
}
