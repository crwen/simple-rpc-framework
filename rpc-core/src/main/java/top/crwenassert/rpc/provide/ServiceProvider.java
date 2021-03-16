package top.crwenassert.rpc.provide;

import top.crwenassert.rpc.domain.dto.RpcServiceProperties;

/**
 * ClassName: ServiceProvider
 * Description: 保存和提供实例对象
 * date: 2021/2/18 23:12
 *
 * @author crwen
 * @create 2021-02-18-23:12
 * @since JDK 1.8
 */
public interface ServiceProvider {

    /**
     *  保存本地服务
     *
     * @param service 服务实体
     * @param serviceName 服务 Class
     * @param <T>
     */
    <T> void addServiceProvider(T service, String serviceName);

    /**
     *  根据服务名称获取服务
     *
     * @param serviceName 服务名称
     * @return
     */
    Object getServiceProvider(String serviceName);

    /**
     *  发布服务
     *
     * @param service 服务
     * @param rpcServiceProperties 服务配置
     * @param <T>
     */
    <T> void publishService(T service, RpcServiceProperties rpcServiceProperties);
}
