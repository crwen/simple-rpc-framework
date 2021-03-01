package top.crwenassert.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Service
 * Description:
 * date: 2021/2/21 18:12
 *
 * @author crwen
 * @create 2021-02-21-18:12
 * @since JDK 1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    String group() default "";

}
