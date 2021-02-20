package top.crwenassert.rpc;

import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.serializer.CommonSerializer;

/**
 * ClassName: RPCClient
 * Description: 客户端通用接口
 * date: 2020/12/12 14:19
 *
 * @author crwen
 * @create 2020-12-12-14:19
 * @since JDK 1.8
 */
public interface RPCClient {

    /**
     *  默认序列化器的序列号，默认为 Kryo
     */
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     *  发送请求
     *  子类需要实现该方法，将请求发送给服务端
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RPCRequest rpcRequest);

}
