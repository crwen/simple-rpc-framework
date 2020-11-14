package top.crwenassert.rpc.server;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            // 读取请求
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            // 获取服务，并调用相应方法
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            // 将调用得到的结果发送出去
            objectOutputStream.writeObject(RPCResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("服务调用或发送时发生异常: ", e);
            throw new RPCException(RPCErrorEnum.SERVICE_INVOCATION_FAILURE);
        }
    }
}
