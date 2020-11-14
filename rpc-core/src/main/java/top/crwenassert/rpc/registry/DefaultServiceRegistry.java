package top.crwenassert.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: DefaultServiceRegistry
 * Description: 默认的服务注册表
 * date: 2020/11/14 22:54
 *
 * @author crwen
 * @create 2020-11-14-22:54
 * @since JDK 1.8
 */
@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry {

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> serviceNames = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (serviceNames.contains(serviceName)) {
            return;
        }
        serviceNames.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        // don't implement any interface
        if (interfaces.length == 0) {
            throw new RPCException(RPCErrorEnum.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> serviceInterface : interfaces) {
            serviceMap.put(serviceInterface.getCanonicalName(), service);
        }
        log.info("向接口：{} 注册服务：{}", interfaces, serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RPCException(RPCErrorEnum.SERVICE_NOT_FOUND, serviceName);
        }
        return service;
    }
}
