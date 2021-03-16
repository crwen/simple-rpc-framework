package top.crwenassert.rpc.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * ClassName: SpringTest
 * Description:
 * date: 2021/3/15 18:41
 *
 * @author crwen
 * @create 2021-03-15-18:41
 * @since JDK 1.8
 */
@SpringJUnitConfig(classes = SpringTest.class)
@Configuration
@ComponentScan("top.crwenassert.rpc.spring")
public class SpringTest {

    @Test
    public void test() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringTest.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String bean : beanDefinitionNames) {
            System.out.println(bean);
        }
    }
}
