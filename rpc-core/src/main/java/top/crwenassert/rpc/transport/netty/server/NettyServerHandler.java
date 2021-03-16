package top.crwenassert.rpc.transport.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.factory.SingletonFactory;
import top.crwenassert.rpc.transport.handler.RequestHandler;

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


    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {

        try {
            if (msg.getHeartBeat()) {
                log.info("接收到客户端心跳包...");
                return;
            }
            log.info("服务器接收到请求: {}", msg);
            // top.crwenassert.rpc.transport.handler
            Object result = requestHandler.handle(msg);
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                // 回送
                ctx.writeAndFlush(RPCResponse.success(result, msg.getRequestId()));
            } else {
                log.error("channel 不可写");
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            // 读空闲
            if (state == IdleState.READER_IDLE) {
                log.info("读空闲，断开连接...");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}