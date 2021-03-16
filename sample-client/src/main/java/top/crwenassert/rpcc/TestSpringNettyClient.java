package top.crwenassert.rpcc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.crwenassert.rpc.annotation.ConsumerScan;
import top.crwenassert.rpcc.controller.TestController;

/**
 * ClassName: TestNettyClient
 * Description:
 * date: 2020/12/13 18:06
 *
 * @author crwen
 * @create 2020-12-13-18:06
 * @since JDK 1.8
 */
@ConsumerScan(basePackage = "top.crwenassert.rpcc")
public class TestSpringNettyClient {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestSpringNettyClient.class);
        TestController controller = context.getBean(TestController.class);
        controller.test();

        for (int i = 0; i < 5; i++) {
            System.out.println(controller.testCache("123"));
        }
        System.out.println(controller.testCache("234"));
    }
}
