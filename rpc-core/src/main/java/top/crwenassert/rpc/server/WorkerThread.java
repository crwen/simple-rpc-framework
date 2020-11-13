package top.crwenassert.rpc.server;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.server.dto.RPCRequest;
import top.crwenassert.rpc.server.dto.RPCResponse;

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
public class WorkerThread implements Runnable {

    private Socket socket;
    private Object service;

    public WorkerThread(Socket socket, Object service) {
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
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object returnObject = method.invoke(service, rpcRequest.getParameters());

            // 将调用得到的结果发送出去
            objectOutputStream.writeObject(RPCResponse.success(returnObject));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("occur exception: ", e);
        }
    }
}
