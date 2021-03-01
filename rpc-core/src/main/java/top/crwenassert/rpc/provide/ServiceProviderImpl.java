package top.crwenassert.rpc.provide;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: DefaultServiceRegistry
 * Description: 默认的服务注册表，保存服务本地服务
 * date: 2020/11/14 22:54
 *
 * @author crwen
 * @create 2020-11-14-22:54
 * @since JDK 1.8
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> serviceNames = ConcurrentHashMap.newKeySet();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if (serviceNames.contains(serviceName)) {
            return;
        }

        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        log.info("向接口：{} 注册服务：{}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RPCException(RPCErrorEnum.SERVICE_NOT_FOUND, serviceName);
        }
        return service;
    }
}
