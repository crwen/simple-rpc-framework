package top.crwenassert.rpc.server;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.ResponseCode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * ClassName: WorkerThread
 * Description:
 * date: 2020/11/13 13:53
 *
 * @author crwen
 * @create 2020-11-13-13:53
 * @since JDK 1.8
 */
@Slf4j
public class RequestHandler implements Runnable {

    private Socket socket;
    private Object service;

    public RequestHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            // 读取请求
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            // 获取方法，并调用
            Object returnObject = invokeMethod(rpcRequest);
            // 将调用得到的结果发送出去
            objectOutputStream.writeObject(RPCResponse.success(returnObject));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            log.error("occur exception: ", e);
        }
    }

    private Object invokeMethod(RPCRequest rpcRequest) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(rpcRequest.getInterfaceName());
        if (!clazz.isAssignableFrom(service.getClass())) {
            return RPCResponse.fail(ResponseCode.CLASS_NOT_FOUND);
        }
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RPCResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
