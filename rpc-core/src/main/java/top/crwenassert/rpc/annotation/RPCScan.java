package top.crwenassert.rpc.annotation;

import org.springframework.context.annotation.Import;
import top.crwenassert.rpc.spring.CustomScannerRegistrar;

import java.lang.annotation.*;

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
@Import({CustomScannerRegistrar.class})
@Inherited
public @interface RPCScan {

    String basePackage() default "";

}
