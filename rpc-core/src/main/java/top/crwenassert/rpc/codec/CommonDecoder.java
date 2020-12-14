package top.crwenassert.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.PackageType;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.util.List;

/**
 * ClassName: CommonDecoder
 * Description: 通用的解码拦截器
 * date: 2020/12/12 14:41
 *
 * @author crwen
 * @create 2020-12-12-14:41
 * @since JDK 1.8
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            log.error("不识别的协议包：{}", magic);
            throw new RPCException(RPCErrorEnum.UNKNOWN_PROTOCOL);
        }
        int packageCode = in.readInt();
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RPCRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RPCResponse.class;
        } else {
            log.error("不识别的数据包：{}", packageCode);
            throw new RPCException(RPCErrorEnum.UNKNOWN_PACKAGE_TYPE);
        }
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            log.error("不识别的序列化器：{}", serializerCode);
            throw new RPCException(RPCErrorEnum.UNKNOWN_SERIALIZER);
        }

        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }
}
