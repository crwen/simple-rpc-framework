package top.crwenassert.rpc.transport.netty.client;

import top.crwenassert.rpc.domain.dto.RPCResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: UnprocessedRequests
 * Description: 用于保存未处理完成的请求
 * date: 2021/2/20 18:34
 *
 * @author crwen
 * @create 2021-02-20-18:34
 * @since JDK 1.8
 */
public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<RPCResponse>> unprocessedResponseFutures = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RPCResponse> future) {
        unprocessedResponseFutures.put(requestId, future);
    }

    /**
     *  根据请求 id 移除 CompletableFuture
     *
     * @param requestId
     */
    public void remove(String requestId) {
        unprocessedResponseFutures.remove(requestId);
    }

    /**
     *  如果请求没有完成，调用 get() 等方法时会返回指定的 RPCResponse 对象
     *
     * @param rpcResponse 指定的 RPCResponse 对象
     */
    public void complete(RPCResponse rpcResponse) {
        CompletableFuture<RPCResponse> future = unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
