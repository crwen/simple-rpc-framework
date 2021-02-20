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

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RPCRequest rpcRequest);

}
