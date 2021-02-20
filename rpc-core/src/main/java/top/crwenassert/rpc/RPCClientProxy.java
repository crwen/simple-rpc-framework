package top.crwenassert.rpc;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RPCRequest rpcRequest = new RPCRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        return client.sendRequest(rpcRequest);
    }
}
