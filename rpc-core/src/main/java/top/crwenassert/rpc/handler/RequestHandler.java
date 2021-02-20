package top.crwenassert.rpc.handler;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.ResponseCode;
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

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RPCRequest rpcRequest) {
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }



    private Object invokeTargetMethod(RPCRequest rpcRequest, Object service){
        Object result;
        try {
            Method method = service.getClass().getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("服务：{} 成功调用方法：{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RPCResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return result;
    }
}
