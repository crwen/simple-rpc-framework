package top.crwenassert.rpc.service;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.annotation.RPCService;
import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;

/**
 * ClassName: HelloServiceImpl
 * Description: 服务提供者 测试
 * date: 2020/11/13 13:33
 *
 * @author crwen
 * @create 2020-11-13-13:33
 * @since JDK 1.8
 */
@Slf4j
@RPCService(group = "hello1")
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(MessageObject messageObject) {
        log.info("HelloServiceImpl 收到消息：{}.", messageObject.getMessage());
        String result = "MessageObject id is " + messageObject.getId();
        log.info("HelloServiceImpl 返回：{}.", result);

        return result;
    }
}
