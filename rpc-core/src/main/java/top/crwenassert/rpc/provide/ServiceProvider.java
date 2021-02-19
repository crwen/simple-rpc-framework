package top.crwenassert.rpc.provide;

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

    <T> void addServiceProvider(T service, Class<T> serviceClass);

    Object getServiceProvider(String serviceName);
}
