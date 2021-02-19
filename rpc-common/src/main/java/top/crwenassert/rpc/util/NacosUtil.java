package top.crwenassert.rpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * ClassName: NacosUtils
 * Description:
 * date: 2021/2/19 21:11
 *
 * @author crwen
 * @create 2021-02-19-21:11
 * @since JDK 1.8
 */
@Slf4j
public class NacosUtil {
    private static final String SERVER_ADDR = "127.0.0.1:8848";


    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接 Nacos 时发生错误：", e);
            throw new RPCException(RPCErrorEnum.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void registerService(NamingService namingService, String serviceName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
    }

    public static List<Instance> getAllInstance(NamingService namingService, String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}
