package top.crwenassert.rpc.transport.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import top.crwenassert.rpc.transport.netty.codec.CommonDecoder;
import top.crwenassert.rpc.transport.netty.codec.CommonEncoder;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: NettyClientChannelInitializer
 * Description: Netty 客户端初始化器
 * date: 2020/12/17 15:31
 *
 * @author crwen
 * @create 2020-12-17-15:31
 * @since JDK 1.8
 */
public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private CommonSerializer serializer;
    private int READER_IDLE_TIME = 0;
    private int WRITER_IDLE_TIME = 5;
    private int ALL_IDLE_TIME = 0;

    public NettyClientChannelInitializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CommonDecoder())
                .addLast(new IdleStateHandler(READER_IDLE_TIME, WRITER_IDLE_TIME, ALL_IDLE_TIME, TimeUnit.SECONDS))
                .addLast(new CommonEncoder(serializer))
                .addLast(new NettyClientHandler());
    }

}
