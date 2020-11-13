package top.crwenassert.rpc.exception;

import top.crwenassert.rpc.domain.enums.RPCErrorEnum;

/**
 * ClassName: RPCException
 * Description:
 * date: 2020/11/13 20:31
 *
 * @author crwen
 * @create 2020-11-13-20:31
 * @since JDK 1.8
 */
public class RPCException extends RuntimeException{

    public RPCException(RPCErrorEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ": " + detail);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCException(RPCErrorEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
