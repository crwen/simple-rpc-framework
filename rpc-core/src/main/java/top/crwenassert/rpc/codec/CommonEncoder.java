package top.crwenassert.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.PackageType;
import top.crwenassert.rpc.serializer.CommonSerializer;

/**
 * ClassName: CommonEncoder
 * Description: 通用的编码拦截器
 * date: 2020/12/12 14:41
 *
 * @author crwen
 * @create 2020-12-12-14:41
 * @since JDK 1.8
 */
@Slf4j
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUMBER);
        //
        if (msg instanceof RPCRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        }else if (msg instanceof RPCResponse) {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        } else {
            log.error("");
            //throw new RPCException(RPCErrorEnum.)
        }

        // 指定序列化器，并序列化
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
