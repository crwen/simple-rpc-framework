package top.crwenassert.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * ClassName: RandomLoadBalancer
 * Description: 随机负载均衡
 * date: 2021/2/21 3:15
 *
 * @author crwen
 * @create 2021-02-21-3:15
 * @since JDK 1.8
 */
public class RandomLoadBalancer implements LoadBalancer{
    private Random random = new Random();
    @Override
    public Instance select(List<Instance> instances) {

        return instances.get(random.nextInt(instances.size()));
    }
}
