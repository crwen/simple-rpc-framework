package top.crwenassert.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Reference
 * Description:
 * date: 2021/2/21 19:43
 *
 * @author crwen
 * @create 2021-02-21-19:43
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Reference {
    String group() default "";
}
