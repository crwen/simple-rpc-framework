package top.crwenassert.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.RPCClient;
import top.crwenassert.rpc.config.PropertiesConfig;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.factory.SingletonFactory;
import top.crwenassert.rpc.loadbalancer.LoadBalancer;
import top.crwenassert.rpc.registry.NacosServiceDiscovery;
import top.crwenassert.rpc.registry.ServiceDiscovery;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * ClassName: NettyClient
 * Description: Socket方式远程方法调用的消费者（客户端）
 * date: 2020/12/12 14:40
 *
 * @author crwen
 * @create 2020-12-12-14:40
 * @since JDK 1.8
 */
@Slf4j
public class NettyClient implements RPCClient {

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup group;
    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    private final UnprocessedRequests unprocessedRequests;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    public NettyClient() {
        this.serviceDiscovery = new NacosServiceDiscovery();
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.serializer = PropertiesConfig.getSerializer();

    }

    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public NettyClient(Integer serializer) {
        if (CommonSerializer.getByCode(serializer) == null) {
            log.error("不支持该序列化器");
            throw new RPCException(RPCErrorEnum.UNKNOWN_SERIALIZER);
        }
        this.serviceDiscovery = new NacosServiceDiscovery();
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    public NettyClient(Integer serializer, LoadBalancer loadBalancer) {
        if (CommonSerializer.getByCode(serializer) == null) {
            log.error("不支持该序列化器");
            throw new RPCException(RPCErrorEnum.UNKNOWN_SERIALIZER);
        }
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RPCResponse> sendRequest(RPCRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCErrorEnum.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RPCResponse> resultFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.toRpcServiceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if (channel.isActive()) {
                unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
                // 发送请求
                channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                    if(future1.isSuccess()) {
                        log.info(String.format("客户端向 【%s】 发送消息: %s", inetSocketAddress, rpcRequest.toString()));
                    } else {
                        future1.channel().close();
                        resultFuture.completeExceptionally(future1.cause());
                        log.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
            } else {
                group.shutdownGracefully();
                return null;
            }
        } catch (InterruptedException e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            log.error("发送消息时有错误发生: ", e);
            Thread.currentThread().interrupt();
        }
        return resultFuture;
    }

}
