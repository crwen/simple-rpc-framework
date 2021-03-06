package top.crwenassert.rpc.cache.lru;

import top.crwenassert.rpc.cache.Cache;
import top.crwenassert.rpc.util.LRUCache;

import java.util.Map;

/**
 * ClassName: LRUCache
 * Description: LRU 缓存
 * date: 2021/3/5 21:12
 *
 * @author crwen
 * @create 2021-03-05-21:12
 * @since JDK 1.8
 */
public class LruCache implements Cache {

    private final Map<Object, Object> cache;

    public LruCache() {
        this.cache = new LRUCache<>();
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return cache.get(key);
    }
}
