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
    void start(int port);

    void setSerializer(CommonSerializer serializer);
}
