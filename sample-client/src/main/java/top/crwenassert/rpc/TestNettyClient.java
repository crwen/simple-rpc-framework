package top.crwenassert.rpc;

import top.crwenassert.rpc.api.ByeService;
import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.transport.netty.client.NettyClient;

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
        RPCClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RPCClientProxy rpcClientProxy = new RPCClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);

        MessageObject object = new MessageObject(12, "This is a meaage");
        String res = helloService.hello(object);
        System.out.println(res);
        res = byeService.bye("client");
        System.out.println(res);
        //for (int i = 0; i < 20; i++) {
        //    String res = proxy.hello(object);
        //    System.out.println(res);
        //}
    }
}
