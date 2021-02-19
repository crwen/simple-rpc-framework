package top.crwenassert.rpc.registry;

import java.net.InetSocketAddress;

/**
 * ClassName: ServiceDiscovery
 * Description: 服务发现接口
 * date: 2021/2/19 21:07
 *
 * @author crwen
 * @create 2021-02-19-21:07
 * @since JDK 1.8
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名称查找服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    InetSocketAddress lookupService(String serviceName);

}
