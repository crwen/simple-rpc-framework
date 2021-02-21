package top.crwenassert.rpc;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.transport.netty.client.NettyClient;
import top.crwenassert.rpc.transport.socket.client.SocketClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * ClassName: RPCClientProxy
 * Description:
 * date: 2020/11/13 15:27
 *
 * @author crwen
 * @create 2020-11-13-15:27
 * @since JDK 1.8
 */
@Slf4j
public class RPCClientProxy implements InvocationHandler {

    private final RPCClient client;

    public RPCClientProxy(RPCClient client) {
        this.client = client;
    }

    /**
     *  利用 JDK 动态代理获取代理对象
     * @param clazz 要代理的对象
     * @param <T>
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[] {clazz}, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RPCRequest rpcRequest = RPCRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .heartBeat(false)
                .build();

        RPCResponse rpcResponse = null;

        if (client instanceof NettyClient) {
            CompletableFuture<RPCResponse> completableFuture = ((NettyClient) client).sendRequest(rpcRequest);
            try {
                rpcResponse = completableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("方法调用请求发送失败", e);
                return null;
            }
        }

        if (client instanceof SocketClient) {
            rpcResponse = (RPCResponse) client.sendRequest(rpcRequest);
        }
        return rpcResponse.getData();
    }
}
