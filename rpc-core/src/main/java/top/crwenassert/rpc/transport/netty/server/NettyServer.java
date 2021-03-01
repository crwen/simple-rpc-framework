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
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.hook.ShutdownHook;
import top.crwenassert.rpc.provide.ServiceProviderImpl;
import top.crwenassert.rpc.registry.NacosServiceRegistry;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.transport.AbstractRPCServer;

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
public class NettyServer extends AbstractRPCServer {

    // 序列化器
    private final CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer) {
        if (CommonSerializer.getByCode(serializer) == null) {
            log.error("不支持该序列化器");
            throw new RPCException(RPCErrorEnum.UNKNOWN_SERIALIZER);
        }
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }

    @Override
    public void start() {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCErrorEnum.SERIALIZER_NOT_FOUND);
        }
        // 启动前先清除服务
        ShutdownHook.getShutdownHook().addClearAllHook();
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



}
