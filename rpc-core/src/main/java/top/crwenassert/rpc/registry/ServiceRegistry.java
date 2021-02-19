package top.crwenassert.rpc.registry;

import java.net.InetSocketAddress;

/**
 * ClassName: ServiceRegistry
 * Description: 服务注册表通用接口
 * date: 2020/11/14 22:52
 *
 * @author crwen
 * @create 2020-11-14-22:52
 * @since JDK 1.8
 */
public interface ServiceRegistry {

    /**
     *  将一个服务注册进服务注册到中心
     *
     * @param serviceName 服务名称
     * @param inetSocketAddress 服务地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);


}
