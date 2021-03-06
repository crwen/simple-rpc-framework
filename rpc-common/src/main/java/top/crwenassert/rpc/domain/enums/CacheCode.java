package top.crwenassert.rpc.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: CacheCode
 * Description:
 * date: 2021/3/5 22:22
 *
 * @author crwen
 * @create 2021-03-05-22:22
 * @since JDK 1.8
 */
@Getter
@AllArgsConstructor
public enum  CacheCode {
    LRU(0);
    private final int code;
}
