package top.crwenassert.rpcc.controller;

import org.springframework.stereotype.Controller;
import top.crwenassert.rpc.annotation.Reference;
import top.crwenassert.rpc.api.CachedService;
import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;

/**
 * ClassName: TestController
 * Description:
 * date: 2021/3/16 1:58
 *
 * @author crwen
 * @create 2021-03-16-1:58
 * @since JDK 1.8
 */
@Controller
public class TestController {
    @Reference(group = "hello1")
    private HelloService helloService;
    @Reference
    private CachedService cachedService;

    public void test() {

        MessageObject object = new MessageObject(12, "This is a meaage");
        String res = helloService.hello(object);
        System.out.println("hello......" + res);
    }

    public String testCache(String id) {
        return cachedService.findCache(id);
    }
}
