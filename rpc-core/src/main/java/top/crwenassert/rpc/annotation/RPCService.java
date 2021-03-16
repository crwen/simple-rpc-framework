package top.crwenassert.rpc.annotation;

import java.lang.annotation.*;

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
public @interface RPCService {

    String group() default "";

}
