package top.crwenassert.rpc.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import top.crwenassert.rpc.RPCClientProxy;
import top.crwenassert.rpc.annotation.Cached;
import top.crwenassert.rpc.annotation.RPCService;
import top.crwenassert.rpc.annotation.Reference;
import top.crwenassert.rpc.domain.dto.Invocation;
import top.crwenassert.rpc.domain.dto.RpcServiceProperties;
import top.crwenassert.rpc.factory.SingletonFactory;
import top.crwenassert.rpc.provide.CacheProvider;
import top.crwenassert.rpc.provide.CacheProviderImpl;
import top.crwenassert.rpc.provide.ServiceProvider;
import top.crwenassert.rpc.provide.ServiceProviderImpl;
import top.crwenassert.rpc.transport.netty.client.NettyClient;
import top.crwenassert.rpc.util.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ClassName: SpringBeanPostProcessor
 * Description:
 * date: 2021/3/15 19:34
 *
 * @author crwen
 * @create 2021-03-15-19:34
 * @since JDK 1.8
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;
    private final CacheProvider cacheProvider;
    private final NettyClient rpcClient;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
        this.cacheProvider = SingletonFactory.getInstance(CacheProviderImpl.class);
        rpcClient = new NettyClient();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RPCService.class)) {
            RPCService rpcService = bean.getClass().getAnnotation(RPCService.class);
            RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                    .serviceName(bean.getClass().getInterfaces()[0].getCanonicalName())
                    .group(rpcService.group())
                    .build();
            serviceProvider.publishService(bean, rpcServiceProperties);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        injectReference(bean, declaredFields);

        Method[] declaredMethods = bean.getClass().getDeclaredMethods();
        if (!ArrayUtils.isEmpty(declaredMethods)) {
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Cached.class)) {
                    Cached cached = method.getAnnotation(Cached.class);
                    Invocation invocation = Invocation.builder()
                            .paramTypes(method.getParameterTypes())
                            .parameters(method.getParameters())
                            .methodName(method.getName())
                            .cacheCode(cached.type())
                            .build();
                    cacheProvider.addCache(invocation);
                }
            }
        }
        return bean;
    }

    private void injectReference(Object bean, Field[] declaredFields) {
        if (!ArrayUtils.isEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Reference.class)) {
                    Reference reference = field.getAnnotation(Reference.class);
                    if (reference != null) {
                        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                                .serviceName(field.getType().getCanonicalName())
                                .group(reference.group())
                                .build();
                        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcClient, rpcServiceProperties);
                        Object clientProxy = rpcClientProxy.getProxy(field.getType());
                        ClassUtil.setField(field, bean, clientProxy);
                    }
                }
            }
        }
    }

}
