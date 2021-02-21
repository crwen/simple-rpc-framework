package top.crwenassert.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * ClassName: RoundRobinLoadBalancer
 * Description: 负载均衡 轮询
 * date: 2021/2/21 3:10
 *
 * @author crwen
 * @create 2021-02-21-3:10
 * @since JDK 1.8
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private int index = 0;

    @Override
    public Instance select(List<Instance> instances) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index ++);
    }
}
