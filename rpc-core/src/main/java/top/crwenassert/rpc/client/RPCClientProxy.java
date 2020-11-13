package top.crwenassert.rpc.client;

import top.crwenassert.rpc.server.dto.RPCRequest;
import top.crwenassert.rpc.server.dto.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ClassName: RPCClientProxy
 * Description:
 * date: 2020/11/13 15:27
 *
 * @author crwen
 * @create 2020-11-13-15:27
 * @since JDK 1.8
 */
public class RPCClientProxy implements InvocationHandler {

    private String host;
    private int port;

    public RPCClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[] {clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建 RPCRequest 请求对象
        RPCRequest rpcRequest = RPCRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RPCClient rpcClient = new RPCClient();
        // 发送请求
        return ((RPCResponse) rpcClient.sendRequest(rpcRequest, host, port)).getData();
    }
}
