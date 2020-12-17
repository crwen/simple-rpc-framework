package top.crwenassert.rpc.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import top.crwenassert.rpc.codec.CommonDecoder;
import top.crwenassert.rpc.codec.CommonEncoder;
import top.crwenassert.rpc.serializer.CommonSerializer;

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

    public NettyClientChannelInitializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CommonDecoder())
                .addLast(new CommonEncoder(serializer))
                .addLast(new NettyClientHandler());
    }

}
