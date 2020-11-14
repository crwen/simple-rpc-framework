package top.crwenassert.rpc.registry;

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
     *  将服务注册到注册表
     * @param service
     * @param <T>
     */
    <T> void register(T service);

    /**
     *  根据服务名称获取服务实体
     * @param serviceName
     * @return
     */
    Object getService(String serviceName);
}
