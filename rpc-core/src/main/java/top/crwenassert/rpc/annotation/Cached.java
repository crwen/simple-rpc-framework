package top.crwenassert.rpc.annotation;

import top.crwenassert.rpc.domain.enums.CacheCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Cached
 * Description:
 * date: 2021/3/5 22:28
 *
 * @author crwen
 * @create 2021-03-05-22:28
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cached {
    CacheCode type() default CacheCode.LRU;
}
