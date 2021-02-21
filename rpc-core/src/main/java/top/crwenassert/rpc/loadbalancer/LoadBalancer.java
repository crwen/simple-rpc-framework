package top.crwenassert.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * ClassName: LoadBalancer
 * Description: 负载均衡接口
 * date: 2021/2/21 3:07
 *
 * @author crwen
 * @create 2021-02-21-3:07
 * @since JDK 1.8
 */
public interface LoadBalancer {

    /**
     *  从实例列表中选择一个实例
     *
     * @param instances 实例列表
     * @return 选择的实例
     */
    Instance select(List<Instance> instances);
}
