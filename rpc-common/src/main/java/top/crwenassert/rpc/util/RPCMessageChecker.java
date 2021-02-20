package top.crwenassert.rpc.util;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;

/**
 * ClassName: RPCMessageChecker
 * Description: 检查响应与请求工具类
 * date: 2020/12/17 16:50
 *
 * @author crwen
 * @create 2020-12-17-16:50
 * @since JDK 1.8
 */
@Slf4j
public class RPCMessageChecker {

    private static final String INTERFACE_NAME = "interfaceName";

    private RPCMessageChecker() {}

    /**
     *  检查请求与响应是否匹配
     * @param rpcRequest 请求
     * @param rpcResponse 响应
     */
    public static void check(RPCRequest rpcRequest, RPCResponse rpcResponse) {
        if (rpcResponse == null) {
            log.error("调用服务失败，serviceName：{}", rpcRequest.getInterfaceName());
            throw new RPCException(RPCErrorEnum.RESPONSE_NOT_MATCH,
                    INTERFACE_NAME + ": " + rpcRequest.getInterfaceName());
        }

        // 检查 id
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            log.error("响应与请求不匹配,requestId:{},responseId:{}", rpcRequest.getRequestId(), rpcResponse.getRequestId());
            throw new RPCException(RPCErrorEnum.RESPONSE_NOT_MATCH,
                    INTERFACE_NAME + ": " + rpcRequest.getInterfaceName());
        }

        // 检查状态
        if (rpcResponse.getStatusCode() == null) {
            log.error("调用服务失败,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RPCException(RPCErrorEnum.SERVICE_INVOCATION_FAILURE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
