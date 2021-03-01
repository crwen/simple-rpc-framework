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
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("未设置序列化器"),
    RESPONSE_NOT_MATCH("响应与请求不匹配"),
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("连接注册中心失败"),
    REGISTER_SERVICE_FAILED("注册服务失败"),
    UNKNOWN_ERROR("出现未知错误"),
    SERVICE_SCAN_PACKAGE_NOT_FOUND("启动类ServiceScan注解缺失"),
    ;

    private final String message;
}
