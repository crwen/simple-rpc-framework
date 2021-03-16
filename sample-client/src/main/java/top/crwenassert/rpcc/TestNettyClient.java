package top.crwenassert.rpcc;

import top.crwenassert.rpc.RPCClient;
import top.crwenassert.rpc.RPCClientProxy;
import top.crwenassert.rpc.annotation.ConsumerScan;
import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;
import top.crwenassert.rpc.domain.dto.RpcServiceProperties;
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
@ConsumerScan(basePackage = "top.crwenassert.rpcc")
public class TestNettyClient {

    public static void main(String[] args) {
        //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestNettyClient.class);
        //TestController controller = context.getBean(TestController.class);
        //controller.test();
        RPCClient client = new NettyClient();
        RpcServiceProperties properties = RpcServiceProperties.builder()
                .group("hello2")
                .serviceName("helloService")
                .build();
        RPCClientProxy rpcClientProxy = new RPCClientProxy(client, properties);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        MessageObject object = new MessageObject(12, "This is a meaage");
        String result = helloService.hello(object);
        System.out.println(result);

        //RpcServiceProperties properties1 = RpcServiceProperties.builder()
        //        .group("properties")
        //        .serviceName("byeService")
        //        .build();
        //ByeService byeService = rpcClientProxy.getProxy(properties1, ByeService.class);
        //helloService.hello(object);
        //byeService.bye("bye");
        //HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        //ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        //CachedService cachedService = rpcClientProxy.getProxy(CachedService.class);
        //MessageObject object = new MessageObject(12, "This is a meaage");
        //String res = helloService.hello(object);
        //System.out.println(res);
        //String result = cachedService.findCache("10086");
        //System.out.println(result);
        //for (int i = 0; i < 1010; i++) {
        //    result = cachedService.findCache(String.valueOf("10086"));
        //}
        //for (int i = 0; i < 1010; i++) {
        //    result = cachedService.findCache(String.valueOf(i));
        //}
        //System.out.println(result);
        //res = byeService.bye("client");
        //System.out.println(res);

        //for (int i = 0; i < 20; i++) {
        //    String res = proxy.hello(object);
        //    System.out.println(res);
        //}
    }
}
