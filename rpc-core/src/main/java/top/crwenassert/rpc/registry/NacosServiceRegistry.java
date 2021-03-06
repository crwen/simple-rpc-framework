package top.crwenassert.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.util.NacosUtil;

import java.net.InetSocketAddress;

/**
 * ClassName: NacosServiceRegistry
 * Description: Nacos 服务注册中心
 * date: 2021/2/18 23:20
 *
 * @author crwen
 * @create 2021-02-18-23:20
 * @since JDK 1.8
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry{

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务时发生错误：", e);
            throw new RPCException(RPCErrorEnum.REGISTER_SERVICE_FAILED);
        }
    }
}
