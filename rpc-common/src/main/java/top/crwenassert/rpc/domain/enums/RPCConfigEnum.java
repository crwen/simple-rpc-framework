package top.crwenassert.rpc.domain.enums;

import lombok.Getter;

/**
 * ClassName: RPCConfigEnum
 * Description:
 * date: 2021/3/15 20:13
 *
 * @author crwen
 * @create 2021-03-15-20:13
 * @since JDK 1.8
 */
@Getter
public enum RPCConfigEnum {
    HOST("host"),
    PORT("port"),
    RPC_CONFIG_PATH("rpc.properties"),
    NACOS_ADDRESS("rpc.nacos.address"),
    RPC_SERIALIZER("rpc.serializer");
    String propertyValue;

    RPCConfigEnum(String propertyValue) {
        this.propertyValue = propertyValue;
    }

}
