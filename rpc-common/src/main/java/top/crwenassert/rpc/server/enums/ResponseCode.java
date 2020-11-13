package top.crwenassert.rpc.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: ResponseCode
 * Description:
 * date: 2020/11/13 14:03
 *
 * @author crwen
 * @create 2020-11-13-14:03
 * @since JDK 1.8
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS(200, "success"),
    FAIL(500, "fail"),
    NOT_FOUND_METHOD(404, "cannot find method"),
    NOT_FOUND_CLASS(404, "cannot find class")

    ;

    private final int code;
    private final String message;
}
