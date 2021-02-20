package top.crwenassert.rpc.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.RPCServer;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.hook.ShutdownHook;
import top.crwenassert.rpc.provide.ServiceProvider;
import top.crwenassert.rpc.provide.ServiceProviderImpl;
import top.crwenassert.rpc.registry.NacosServiceRegistry;
import top.crwenassert.rpc.registry.ServiceRegistry;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;

/**
 * ClassName: NettyServer
 * Description: Netty 方式远程方法调用的服务提供者（服务端）
 * date: 2020/12/12 14:26
 *
 * @author crwen
 * @create 2020-12-12-14:26
 * @since JDK 1.8
 */
@Slf4j
public class NettyServer implements RPCServer {

    private final String host;
    private final int port;
    // 服务注册中心
    private final ServiceRegistry serviceRegistry;
    // 本地服务提供者
    private final ServiceProvider serviceProvider;

    // 序列化器
    private final CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    @Override
    public void start() {
        ShutdownHook.getShutdownHook().addClearAllHook();
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCErrorEnum.SERIALIZER_NOT_FOUND);
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new NettyServerChannelInitializer(serializer));

            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCErrorEnum.SERIALIZER_NOT_FOUND);
        }
        // 将服务保存到本地
        serviceProvider.addServiceProvider(service, serviceClass);
        // 将服务注册到服务中心
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }
}
