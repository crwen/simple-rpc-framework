package top.crwenassert.rpc.service;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;

@Slf4j
public class HelloServiceImpl2 implements HelloService {

    @Override
    public String hello(MessageObject messageObject) {
        log.info("HelloServiceImpl2 收到消息：{}.", messageObject.getMessage());
        String result = "MessageObject id is " + messageObject.getId();
        log.info("HelloServiceImpl2 返回：{}.", result);
        return result;
    }
}
