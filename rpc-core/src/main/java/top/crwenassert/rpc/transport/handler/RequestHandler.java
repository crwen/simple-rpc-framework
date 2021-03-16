package top.crwenassert.rpc.transport.handler;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.cache.Cache;
import top.crwenassert.rpc.domain.dto.Invocation;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.ResponseCode;
import top.crwenassert.rpc.provide.CacheProvider;
import top.crwenassert.rpc.provide.CacheProviderImpl;
import top.crwenassert.rpc.provide.ServiceProvider;
import top.crwenassert.rpc.provide.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ClassName: WorkerThread
 * Description:
 * date: 2020/11/13 13:53
 *
 * @author crwen
 * @create 2020-11-13-13:53
 * @since JDK 1.8
 */
@Slf4j
public class RequestHandler {
    private static final ServiceProvider serviceProvider;
    private static final CacheProvider cacheProvider;
    static {
        serviceProvider = new ServiceProviderImpl();
        cacheProvider = new CacheProviderImpl();
    }

    /**
     *  处理 rpc 请求
     * @param rpcRequest rpc 请求
     * @return 请求执行结果
     */
    public Object handle(RPCRequest rpcRequest) {
        Object service = serviceProvider.getServiceProvider(rpcRequest.toRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     *  调用目标方法
     *
     * @param rpcRequest rpc 请求
     * @param service 服务
     * @return 方法执行结构
     */
    private Object invokeTargetMethod(RPCRequest rpcRequest, Object service){
        Object result;
        try {
            Class<?> clazz = service.getClass();
            Method method = clazz.getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());

            Invocation invocation = Invocation.builder()
                    .paramTypes(method.getParameterTypes())
                    .parameters(rpcRequest.getParameters())
                    .methodName(method.getName())
                    .build();
            Cache cache = cacheProvider.getCache(invocation.toNameString());
            if (cache != null) {
                result = cache.get(invocation.toFullString());
                if (result != null) {
                    log.info("从缓存中成功调用方法：{}", invocation.toFullString());
                    return result;
                }
            }
            // 执行方法
            result = method.invoke(service, rpcRequest.getParameters());
            if (cache != null) {
                cache.put(invocation.toFullString(), result);
            }
            log.info("服务：{} 成功调用方法：{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RPCResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return result;
    }
}
