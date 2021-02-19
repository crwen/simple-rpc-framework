package top.crwenassert.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    private static final int MAX_RETRY_COUNT = 5;
    private static Channel channel = null;

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) {
        bootstrap.handler(new NettyClientChannelInitializer(serializer));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            connect(bootstrap, inetSocketAddress, countDownLatch);
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("获取 channel 是发生错误：", e);
        }
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
     * @param countDownLatch 用于重连计数
     */
    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress,
                                CountDownLatch countDownLatch) {
        connect(bootstrap,inetSocketAddress,MAX_RETRY_COUNT, countDownLatch);
    }

    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, int retry,
                                CountDownLatch countDownLatch) {
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接成功！");
                channel = future.channel();
                countDownLatch.countDown();
                return;
            } else {
                if (retry == 0) {
                    log.error("客户端连接失败：重试次数已用完，放弃连接！");
                    countDownLatch.countDown();
                    throw new RPCException(RPCErrorEnum.CLIENT_CONNECT_SERVER_FAILURE);
                }
                // 第几次重试
                int order = (MAX_RETRY_COUNT - retry) + 1;
                int delay = 1 << order;
                log.error("{}：连接失败，第 {} 次重试......", new Date(), order);

                // 延迟执行 delay
                bootstrap.config().group().schedule( () -> {
                    connect(bootstrap, inetSocketAddress, retry - 1, countDownLatch);
                }, delay, TimeUnit.SECONDS);
            }
        });

    }
}
