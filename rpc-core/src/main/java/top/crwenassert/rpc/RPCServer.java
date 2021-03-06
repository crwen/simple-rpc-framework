package top.crwenassert.rpc;

import top.crwenassert.rpc.serializer.CommonSerializer;

/**
 * ClassName: RPCServer
 * Description: 服务端通用接口
 * date: 2020/12/12 14:19
 *
 * @author crwen
 * @create 2020-12-12-14:19
 * @since JDK 1.8
 */
public interface RPCServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     *  启动服务
     *
     */
    void start();

    /**
     *  发布服务
     *
     * @param service 服务
     * @param serviceName 服务名称
     * @param <T>
     */
    <T> void publishService(T service, String serviceName);

}
