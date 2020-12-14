package top.crwenassert.rpc;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.domain.enums.ResponseCode;
import top.crwenassert.rpc.exception.RPCException;

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

    public Object handle(RPCRequest rpcRequest, Object service) {
        Object result = null;
        try {
            result = invokeTargetMethod(rpcRequest, service);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("调用或发送时发生错误：", e);
            throw new RPCException(RPCErrorEnum.SERVICE_INVOCATION_FAILURE);
        }
        return result;
    }



    private Object invokeTargetMethod(RPCRequest rpcRequest, Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = service.getClass().getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            log.info("服务：{} 成功调用方法：{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException e) {
            return RPCResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
