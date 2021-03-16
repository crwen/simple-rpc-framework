package top.crwenassert.rpc.transport;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.RPCServer;
import top.crwenassert.rpc.annotation.Cached;
import top.crwenassert.rpc.annotation.ProviderScan;
import top.crwenassert.rpc.annotation.RPCScan;
import top.crwenassert.rpc.annotation.RPCService;
import top.crwenassert.rpc.domain.dto.Invocation;
import top.crwenassert.rpc.domain.dto.RpcServiceProperties;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.provide.CacheProvider;
import top.crwenassert.rpc.provide.ServiceProvider;
import top.crwenassert.rpc.registry.ServiceRegistry;
import top.crwenassert.rpc.util.ClassUtil;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Set;

/**
 * ClassName: AbstractRpcServer
 * Description:
 * date: 2021/2/21 18:36
 *
 * @author crwen
 * @create 2021-02-21-18:36
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractRPCServer implements RPCServer {

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;
    protected CacheProvider cacheProvider;

    public void scanServices() {
        String mainClassName = ClassUtil.getStackTrace();
        Class<?> startClass = null;
        try {
            startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(RPCScan.class) && !startClass.isAnnotationPresent(ProviderScan.class)) {
                log.error("启动类缺少 @RPCScan 注解");
                throw new RPCException(RPCErrorEnum.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RPCException(RPCErrorEnum.UNKNOWN_ERROR);
        }
        String basePackage = "";
        if (startClass.isAnnotationPresent(RPCScan.class)) {
            basePackage = startClass.getAnnotation(RPCScan.class).basePackage();
        } else if (startClass.isAnnotationPresent(ProviderScan.class)) {
            basePackage = startClass.getAnnotation(ProviderScan.class).basePackage();
        }
        if ("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(RPCService.class)) {
                RPCService serviceAnno = clazz.getAnnotation(RPCService.class);
                RpcServiceProperties properties = RpcServiceProperties.builder()
                        .group(serviceAnno.group())
                        .serviceName(clazz.getInterfaces()[0].getCanonicalName())
                        .build();
                String serviceName = properties.toRpcServiceName();
                //String serviceName = clazz.getAnnotation(Service.class).group();
                Object obj = ClassUtil.newInstance(clazz, true);
                if ("".equals(serviceAnno.group())) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface : interfaces) {
                        publishService(obj, oneInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }

                Method[] declaredMethods = clazz.getDeclaredMethods();
                if (declaredMethods.length > 0) {
                    for (Method method : declaredMethods) {
                        if (method.isAnnotationPresent(Cached.class)){
                            Cached cacheAnno = method.getAnnotation(Cached.class);
                            Invocation invocation = Invocation.builder()
                                    .paramTypes(method.getParameterTypes())
                                    .parameters(method.getParameters())
                                    .methodName(method.getName())
                                    .cacheCode(cacheAnno.type())
                                    .build();
                            cacheProvider.addCache(invocation);
                        }
                    }
                }
            }
        }
    }


    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }
}
