package top.crwenassert.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: RPCScan
 * Description:
 * date: 2021/2/21 17:43
 *
 * @author crwen
 * @create 2021-02-21-17:43
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RPCScan {

    String basePackage() default "";

}
