package top.crwenassert.rpc.hook;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.factory.ThreadPoolFactory;
import top.crwenassert.rpc.util.NacosUtil;

import java.util.concurrent.ExecutorService;

/**
 * ClassName: ShutdownHook
 * Description:
 * date: 2021/2/20 12:30
 *
 * @author crwen
 * @create 2021-02-20-12:30
 * @since JDK 1.8
 */
@Slf4j
public class ShutdownHook {
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        log.info("关闭后将自动注销所有服务");
        // 在jvm中增加一个关闭的钩子，当jvm关闭的时候 会执行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 注销服务
            NacosUtil.clearRegistry();
            // 关闭线程池
            ThreadPoolFactory.shutDownAll();
        }));
    }
}
