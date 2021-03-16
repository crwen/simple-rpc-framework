package top.crwenassert.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: ConsumerScan
 * Description:
 * date: 2021/3/16 15:38
 *
 * @author crwen
 * @create 2021-03-16-15:38
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RPCScan
public @interface ConsumerScan {
    String basePackage() default "";

}
