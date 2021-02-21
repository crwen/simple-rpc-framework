package top.crwenassert.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * ClassName: ChannelProvider
 * Description: 用于获取 Channel 对象
 * date: 2020/12/18 16:06
 *
 * @author crwen
 * @create 2020-12-18-16:06
 * @since JDK 1.8
 */
@Slf4j
public class ChannelProvider {

    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootstrap();

    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    //private static final int MAX_RETRY_COUNT = 5;

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) throws InterruptedException {
        String key = inetSocketAddress.toString() + serializer.getCode();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channelMap != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        bootstrap.handler(new NettyClientChannelInitializer(serializer));
        Channel channel = null;
        try {
            channel = connect(bootstrap, inetSocketAddress);
        } catch (ExecutionException e) {
            log.error("连接库客户端时发生错误 ", e);
            return null;
        }
        channelMap.put(key, channel);

        return channel;
    }

    /**
     *  初始化启动器 Bootstrap
     * @return
     */
    private static Bootstrap initializeBootstrap() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                // 连接超时时间，超过这个时间仍然连接不上，就表示连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                // TCP 默认开启 Nagle 算法，该算法作用是尽可能发送大数据库，减少网络传输
                .option(ChannelOption.TCP_NODELAY, true);

        return bootstrap;
    }

    /**
     *  建立连接
     * @param bootstrap 启动器
     * @param inetSocketAddress 服务端地址
     */
    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接 【{}】成功！", inetSocketAddress);
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    //private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, int retry,) {
    //    bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
    //        if (future.isSuccess()) {
    //            log.info("客户端连接成功！");
    //            channel = future.channel();
    //            return;
    //        } else {
    //            if (retry == 0) {
    //                log.error("客户端连接失败：重试次数已用完，放弃连接！");
    //                throw new RPCException(RPCErrorEnum.CLIENT_CONNECT_SERVER_FAILURE);
    //            }
    //            // 第几次重试
    //            int order = (MAX_RETRY_COUNT - retry) + 1;
    //            int delay = 1 << order;
    //            log.error("{}：连接失败，第 {} 次重试......", new Date(), order);
    //
    //            // 延迟执行 delay
    //            bootstrap.config().group().schedule( () -> {
    //                connect(bootstrap, inetSocketAddress, retry - 1);
    //            }, delay, TimeUnit.SECONDS);
    //        }
    //    });
    //    return channel;
    //}
}
