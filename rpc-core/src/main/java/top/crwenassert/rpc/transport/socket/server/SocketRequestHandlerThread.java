package top.crwenassert.rpc.transport.socket.server;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.transport.handler.RequestHandler;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.registry.ServiceRegistry;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.transport.socket.util.ObjectReader;
import top.crwenassert.rpc.transport.socket.util.ObjectWriter;

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
public class SocketRequestHandlerThread implements Runnable {

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler,
                                      ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            // 读取请求
            RPCRequest rpcRequest = (RPCRequest) ObjectReader.readObject(inputStream);
            // 获取服务，并调用相应方法
            Object result = requestHandler.handle(rpcRequest);
            // 将调用得到的结果发送出去
            RPCResponse<Object> response = RPCResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer);

        } catch (IOException  e) {
            log.error("服务调用或发送时发生异常: ", e);
            throw new RPCException(RPCErrorEnum.SERVICE_INVOCATION_FAILURE);
        }
    }
}
