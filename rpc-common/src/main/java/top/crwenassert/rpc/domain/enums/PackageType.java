package top.crwenassert.rpc.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: PackageType
 * Description:
 * date: 2020/12/13 17:42
 *
 * @author crwen
 * @create 2020-12-13-17:42
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;
}
