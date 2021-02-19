package top.crwenassert.rpc.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.crwenassert.rpc.api.HelloService;
import top.crwenassert.rpc.api.MessageObject;

@Slf4j
public class HelloServiceImpl2 implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);

    @Override
    public String hello(MessageObject messageObject) {
        log.info("HelloServiceImpl 收到消息：{}.", messageObject.getMessage());
        String result = "本次处理来自 Socket 服务：MessageObject id is " + messageObject.getId();
        log.info("HelloServiceImpl 返回：{}.", result);
        return result;
    }
}
