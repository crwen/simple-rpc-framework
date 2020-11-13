package top.crwenassert.rpc;

import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;
import top.crwenassert.rpc.client.RPCClientProxy;

/**
 * ClassName: TestClient
 * Description:
 * date: 2020/11/13 13:40
 *
 * @author crwen
 * @create 2020-11-13-13:40
 * @since JDK 1.8
 */
public class TestClient {
    public static void main(String[] args) {
        RPCClientProxy proxy = new RPCClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        MessageObject object = new MessageObject(12, "this is message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
