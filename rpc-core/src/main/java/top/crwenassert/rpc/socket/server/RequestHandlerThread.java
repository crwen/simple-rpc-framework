package top.crwenassert.rpc.socket.server;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.RequestHandler;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.registry.ServiceRegistry;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.socket.util.ObjectReader;
import top.crwenassert.rpc.socket.util.ObjectWriter;

import java.io.*;
import java.net.Socket;

/**
 * ClassName: RequestHandlerThread
 * Description:
 * date: 2020/11/14 23:09
 *
 * @author crwen
 * @create 2020-11-14-23:09
 * @since JDK 1.8
 */
@Slf4j
public class RequestHandlerThread implements Runnable {

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler,
                                ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            // 读取请求
            RPCRequest rpcRequest = (RPCRequest) ObjectReader.readObject(inputStream);
            // 获取服务，并调用相应方法
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            // 将调用得到的结果发送出去
            RPCResponse<Object> response = RPCResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer);

        } catch (IOException  e) {
            log.error("服务调用或发送时发生异常: ", e);
            throw new RPCException(RPCErrorEnum.SERVICE_INVOCATION_FAILURE);
        }
    }
}
