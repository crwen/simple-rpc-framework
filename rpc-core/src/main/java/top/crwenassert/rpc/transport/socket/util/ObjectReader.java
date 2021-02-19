package top.crwenassert.rpc.transport.socket.util;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.PackageType;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName: ObjectReader
 * Description: Socket 方式从输入流中读取字节并反序列化
 * date: 2020/12/17 15:44
 *
 * @author crwen
 * @create 2020-12-17-15:44
 * @since JDK 1.8
 */
@Slf4j
public class ObjectReader {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static Object readObject(InputStream inputStream) throws IOException {
        byte[] numberBytes = new byte[4];
        // 读取魔数
        inputStream.read(numberBytes);
        int magic = bytesToInt(numberBytes);
        if (magic != MAGIC_NUMBER) {
            log.error("不识别的协议包：{}", magic);
            throw new RPCException(RPCErrorEnum.UNKNOWN_PROTOCOL);
        }

        // 读取包类型
        inputStream.read(numberBytes);
        int packageCode = bytesToInt(numberBytes);
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RPCRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RPCResponse.class;
        } else {
            log.error("不识别的数据包：{}", packageCode);
            throw new RPCException(RPCErrorEnum.UNKNOWN_PACKAGE_TYPE);
        }

        // 读取序列化号
        inputStream.read(numberBytes);
        int serializerCode = bytesToInt(numberBytes);
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            log.error("不识别的反序列化器：{}", serializerCode);
            throw new RPCException(RPCErrorEnum.UNKNOWN_SERIALIZER);
        }

        // 读取数据长度
        inputStream.read(numberBytes);
        int length = bytesToInt(numberBytes);
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        return serializer.deserialize(bytes, packageClass);
    }

    private static int bytesToInt(byte[] src) {
        int value;
        value = (src[0] & 0xFF)
                | ((src[1] & 0xFF)<<8)
                | ((src[2] & 0xFF)<<16)
                | ((src[3] & 0xFF)<<24);
        return value;
    }
}
