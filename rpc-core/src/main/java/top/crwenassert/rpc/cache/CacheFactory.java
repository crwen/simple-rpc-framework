package top.crwenassert.rpc.cache;

import top.crwenassert.rpc.cache.lru.LruCache;
import top.crwenassert.rpc.domain.enums.CacheCode;

/**
 * ClassName: CacheFactory
 * Description:
 * date: 2021/3/5 23:07
 *
 * @author crwen
 * @create 2021-03-05-23:07
 * @since JDK 1.8
 */
public interface CacheFactory {

    static Cache getCache(CacheCode cacheCode) {
        switch (cacheCode) {
            case LRU:
                return new LruCache();
            default:
                return null;
        }
    }
}
