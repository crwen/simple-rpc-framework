package top.crwenassert.rpc.serializer;

/**
 * ClassName: CommonSerializer
 * Description: 通用序列化/反序列化接口
 * date: 2020/12/12 14:45
 *
 * @author crwen
 * @create 2020-12-12-14:45
 * @since JDK 1.8
 */
public interface CommonSerializer {

    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer PROTOBUF_SERIALIZER = 2;
    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new ProtobufSerializer();
            default:
                return null;
        }
    }

    static CommonSerializer getByName(String name) {
        switch (name) {
            case "KYRO":
                return new KryoSerializer();
            case "JSON":
                return new JsonSerializer();
            case "PROTOBUF":
                return new ProtobufSerializer();
            default:
                return null;
        }
    }
}
