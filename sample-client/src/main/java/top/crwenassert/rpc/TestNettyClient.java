package top.crwenassert.rpc;

import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;
import top.crwenassert.rpc.transport.netty.client.NettyClient;
import top.crwenassert.rpc.serializer.JsonSerializer;

/**
 * ClassName: TestNettyClient
 * Description:
 * date: 2020/12/13 18:06
 *
 * @author crwen
 * @create 2020-12-13-18:06
 * @since JDK 1.8
 */
public class TestNettyClient {
    public static void main(String[] args) {
        RPCClient client = new NettyClient();
        client.setSerializer(new JsonSerializer());
        //client.setSerializer(new KryoSerializer());
        RPCClientProxy rpcClientProxy = new RPCClientProxy(client);
        HelloService proxy = rpcClientProxy.getProxy(HelloService.class);

        MessageObject object = new MessageObject(12, "This is a meaage");
        String res = proxy.hello(object);
        System.out.println(res);
    }
}
