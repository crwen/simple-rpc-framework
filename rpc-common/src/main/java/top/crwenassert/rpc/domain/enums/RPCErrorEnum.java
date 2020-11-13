package top.crwenassert.rpc.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * ClassName: RPCErrorMessageEnum
 * Description:
 * date: 2020/11/13 20:28
 *
 * @author crwen
 * @create 2020-11-13-20:28
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
@ToString
public enum RPCErrorEnum {
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空")
    ;

    private final String message;
}
