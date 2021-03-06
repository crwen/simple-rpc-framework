package top.crwenassert.rpc.service;

import top.crwenassert.rpc.annotation.Cached;
import top.crwenassert.rpc.annotation.Service;
import top.crwenassert.rpc.api.CachedService;
import top.crwenassert.rpc.domain.enums.CacheCode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: CachedService
 * Description:
 * date: 2021/3/5 23:41
 *
 * @author crwen
 * @create 2021-03-05-23:41
 * @since JDK 1.8
 */
@Service
public class CachedServiceImpl implements CachedService {

    private final AtomicInteger i = new AtomicInteger();

    @Cached(type = CacheCode.LRU)
    public String findCache(String id) {
        return "request: " + id + ", response: " + i.getAndIncrement();
    }
}
