package top.crwenassert.rpc.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: SerializerCode
 * Description: 字节楼中标识序列化和反序列化器
 * date: 2020/12/13 18:01
 *
 * @author crwen
 * @create 2020-12-13-18:01
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum  SerializerCode {
    KRYO(0),
    JSON(1),
    PROTOBUF(2);

    private final int code;
}
