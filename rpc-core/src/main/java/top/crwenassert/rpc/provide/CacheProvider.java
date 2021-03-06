package top.crwenassert.rpc.provide;

import top.crwenassert.rpc.cache.Cache;
import top.crwenassert.rpc.domain.dto.Invocation;

/**
 * ClassName: CacheProviderImpl
 * Description:
 * date: 2021/3/5 22:53
 *
 * @author crwen
 * @create 2021-03-05-22:53
 * @since JDK 1.8
 */
public interface CacheProvider {

    void addCache(Invocation invocation);

    Cache getCache(String key);
}
