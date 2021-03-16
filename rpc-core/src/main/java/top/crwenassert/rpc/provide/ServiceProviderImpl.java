package top.crwenassert.rpc.provide;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.config.PropertiesConfig;
import top.crwenassert.rpc.domain.dto.RpcServiceProperties;
import top.crwenassert.rpc.domain.enums.RPCConfigEnum;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.factory.SingletonFactory;
import top.crwenassert.rpc.registry.NacosServiceRegistry;
import top.crwenassert.rpc.registry.ServiceRegistry;
import top.crwenassert.rpc.util.PropertiesFileUtil;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Properties;
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
    private static final Map<String, Class<?>> serviceClassMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();
    private final ServiceRegistry serviceRegistry;

    public ServiceProviderImpl() {
        this.serviceRegistry = SingletonFactory.getInstance(NacosServiceRegistry.class);
    }

    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if (registeredService.contains(serviceName)) {
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

    @Override
    public <T> void publishService(T service, RpcServiceProperties rpcServiceProperties) {
        String serviceName = rpcServiceProperties.toRpcServiceName();
        addServiceProvider(service, serviceName);
        Properties properties = PropertiesFileUtil.readPropertiesFile(RPCConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String host = PropertiesConfig.getHost();
        int port = PropertiesConfig.getPort();
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
        log.info("向接口：{} 注册服务：{}", service.getClass().getInterfaces(), serviceName);
    }
}
