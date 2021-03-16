package top.crwenassert.rpc.annotation;

import org.springframework.context.annotation.Import;
import top.crwenassert.rpc.transport.netty.server.NettyServer;

import java.lang.annotation.*;

/**
 * ClassName: ProviderScan
 * Description:
 * date: 2021/3/16 15:32
 *
 * @author crwen
 * @create 2021-03-16-15:32
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RPCScan
@Import(NettyServer.class)
@Inherited
public @interface ProviderScan {
    String basePackage() default "";

}
