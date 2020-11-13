package top.crwenassert.rpc.client;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.server.dto.RPCRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClassName: RPCClient
 * Description: 远程方法调用的消费者
 * date: 2020/11/13 14:56
 *
 * @author crwen
 * @create 2020-11-13-14:56
 * @since JDK 1.8
 */
@Slf4j
public class RPCClient {

    public Object sendRequest(RPCRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception: ", e);
            return null;
        }
    }
}
