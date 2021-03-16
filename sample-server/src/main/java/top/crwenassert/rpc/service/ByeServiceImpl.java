package top.crwenassert.rpc.service;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.annotation.RPCService;
import top.crwenassert.rpc.api.ByeService;

/**
 * ClassName: ByeServiceImpl
 * Description:
 * date: 2021/3/1 17:44
 *
 * @author crwen
 * @create 2021-03-01-17:44
 * @since JDK 1.8
 */
@Slf4j
@RPCService
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        log.info("ByeServiceImpl 收到消息：{}.", name);
        String result = "bye  " + name;
        log.info("ByeServiceImpl 返回：{}.", result);
        return result;
    }
}
