package top.crwenassert.rpc.transport.socket.util;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.PackageType;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ClassName: ObjectWriter
 * Description: Socket 方式从输入流中写入字节并序列化
 * date: 2020/12/17 15:53
 *
 * @author crwen
 * @create 2020-12-17-15:53
 * @since JDK 1.8
 */
@Slf4j
public class ObjectWriter {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer) throws IOException {
        outputStream.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RPCRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else if (object instanceof RPCResponse) {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        } else {
            log.error("不识别的数据包：{}", object);
            throw new RPCException(RPCErrorEnum.UNKNOWN_PACKAGE_TYPE);
        }

        outputStream.write(intToBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

    private static byte[] intToBytes(int value) {
        byte[] des = new byte[4];
        des[3] =  (byte) ((value>>24) & 0xFF);
        des[2] =  (byte) ((value>>16) & 0xFF);
        des[1] =  (byte) ((value>>8) & 0xFF);
        des[0] =  (byte) (value & 0xFF);
        return des;
    }
}