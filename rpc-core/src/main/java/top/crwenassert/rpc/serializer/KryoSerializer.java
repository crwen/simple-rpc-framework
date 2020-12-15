package top.crwenassert.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.SerializerCode;
import top.crwenassert.rpc.exception.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ClassName: KryoSerializer
 * Description:
 * date: 2020/12/15 15:27
 *
 * @author crwen
 * @create 2020-12-15-15:27
 * @since JDK 1.8
 */
@Slf4j
public class KryoSerializer implements CommonSerializer {

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RPCResponse.class);
        kryo.register(RPCRequest.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            log.error("序列化时发生错误", e);
            throw new SerializeException("序列化时发生错误");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream);) {
            Kryo kryo = kryoThreadLocal.get();
            Object obj = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return obj;
        } catch (IOException e) {
            log.error("反序列化时发生错误", e);
            throw new SerializeException("反序列化时发生错误");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
