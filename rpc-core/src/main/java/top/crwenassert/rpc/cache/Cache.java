package top.crwenassert.rpc.cache;

/**
 * ClassName: Cache
 * Description: 缓存接口，用于缓存结果
 * date: 2021/3/5 21:09
 *
 * @author crwen
 * @create 2021-03-05-21:09
 * @since JDK 1.8
 */
public interface Cache {

    /**
     *  存储键值对的 API
     * @param key
     * @param value
     */
    void put(Object key, Object value);

    /**
     *  根据 key 返回 value
     * @param key
     * @return
     */
    Object get(Object key);



}
