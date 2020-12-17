package top.crwenassert.rpc.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import top.crwenassert.rpc.codec.CommonDecoder;
import top.crwenassert.rpc.codec.CommonEncoder;
import top.crwenassert.rpc.serializer.CommonSerializer;

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

    // 序列化器
    private CommonSerializer serializer;

    public NettyServerChannelInitializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CommonEncoder(serializer));
        pipeline.addLast(new CommonDecoder());
        pipeline.addLast(new NettyServerHandler());
    }
}
