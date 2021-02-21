package top.crwenassert.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.util.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * ClassName: NacosServiceDiscovery
 * Description: Nacos 服务发现工具
 * date: 2021/2/19 21:10
 *
 * @author crwen
 * @create 2021-02-19-21:10
 * @since JDK 1.8
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery{

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            if (instances == null || instances.size() == 0) {
                log.error("找不到该服务：", serviceName);
                throw new RPCException(RPCErrorEnum.SERVICE_NOT_FOUND);
            }
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时发生错误：", e);
            throw new RPCException(RPCErrorEnum.SERVICE_NOT_FOUND);
        }
    }
}
