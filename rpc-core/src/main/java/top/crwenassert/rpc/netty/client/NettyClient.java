package top.crwenassert.rpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.RPCClient;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.util.RPCMessageChecker;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

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

    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    private CommonSerializer serializer;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public Object sendRequest(RPCRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCErrorEnum.SERIALIZER_NOT_FOUND);
        }

        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            Channel channel = ChannelProvider.get(new InetSocketAddress(host, port), serializer);
            if (channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        log.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RPCResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RPCResponse rpcResponse = channel.attr(key).get();
                RPCMessageChecker.check(rpcRequest, rpcResponse);
                return rpcResponse.getData();
            } else {
                System.exit(0);
            }

        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: ", e);
        }
        return result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
