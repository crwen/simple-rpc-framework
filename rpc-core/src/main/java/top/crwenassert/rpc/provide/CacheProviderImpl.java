package top.crwenassert.rpc.provide;

import top.crwenassert.rpc.cache.Cache;
import top.crwenassert.rpc.cache.CacheFactory;
import top.crwenassert.rpc.domain.dto.Invocation;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: CacheFactory
 * Description:
 * date: 2021/3/5 22:52
 *
 * @author crwen
 * @create 2021-03-05-22:52
 * @since JDK 1.8
 */
public class CacheProviderImpl implements CacheProvider {
    private static final Map<String, Cache> caches = new ConcurrentHashMap<>();
    private static final Set<String> cacheNames = ConcurrentHashMap.newKeySet();


    @Override
    public void addCache(Invocation invocation) {
        String name = invocation.toNameString();
        caches.put(name, CacheFactory.getCache(invocation.getCacheCode()));
        cacheNames.add(name);
    }

    @Override
    public Cache getCache(String key) {
        return caches.get(key);
    }
}
