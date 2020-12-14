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
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
