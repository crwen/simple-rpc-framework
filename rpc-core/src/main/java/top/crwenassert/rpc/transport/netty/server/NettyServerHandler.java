package top.crwenassert.rpc.transport.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.factory.SingletonFactory;
import top.crwenassert.rpc.handler.RequestHandler;
import top.crwenassert.rpc.factory.ThreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * ClassName: NettyServerHandler
 * Description: Netty 服务端处理器
 * date: 2020/12/12 14:33
 *
 * @author crwen
 * @create 2020-12-12-14:33
 * @since JDK 1.8
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    private final RequestHandler requestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private final ExecutorService threadPool;


    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
        this.threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
        threadPool.execute(() -> {
            try {
                log.info("服务器接收到请求: {}", msg);
                // handler
                Object result = requestHandler.handle(msg);
                // 回送
                ChannelFuture future = ctx.writeAndFlush(RPCResponse.success(result, msg.getRequestId()));
                future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}