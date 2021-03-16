package top.crwenassert.rpc.transport.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import top.crwenassert.rpc.transport.netty.codec.CommonDecoder;
import top.crwenassert.rpc.transport.netty.codec.CommonEncoder;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: NettyServerChannelInitializer
 * Description: Netty 服务端初始化器
 * date: 2020/12/17 15:23
 *
 * @author crwen
 * @create 2020-12-17-15:23
 * @since JDK 1.8
 */
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private int READER_IDLE_TIME = 30;
    private int WRITER_IDLE_TIME = 0;
    private int ALL_IDLE_TIME = 0;

    // 序列化器
    private CommonSerializer serializer;

    public NettyServerChannelInitializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 30s 没有读请求就关闭连接
        pipeline.addLast(new IdleStateHandler(READER_IDLE_TIME, WRITER_IDLE_TIME, ALL_IDLE_TIME, TimeUnit.SECONDS));
        pipeline.addLast(new CommonEncoder(serializer));
        pipeline.addLast(new CommonDecoder());
        pipeline.addLast(new NettyServerHandler());
    }
}
