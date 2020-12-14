package top.crwenassert.rpc.socket.client;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.RPCClient;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.domain.enums.ResponseCode;
import top.crwenassert.rpc.exception.RPCException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClassName: RPCClient
 * Description: Socket方式远程方法调用的消费者（客户端）
 * date: 2020/11/13 14:56
 *
 * @author crwen
 * @create 2020-11-13-14:56
 * @since JDK 1.8
 */
@Slf4j
public class SocketClient implements RPCClient {

    private final String host;
    private final int port;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object sendRequest(RPCRequest rpcRequest) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            RPCResponse rpcResponse = (RPCResponse) objectInputStream.readObject();
            // check
            if (rpcResponse == null) {
                log.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RPCException(RPCErrorEnum.SERVICE_INVOCATION_FAILURE,
                        " service: " + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                log.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RPCException(RPCErrorEnum.SERVICE_INVOCATION_FAILURE,
                        " service: " + rpcRequest.getInterfaceName());
            }

            return rpcResponse.getData();
        } catch (IOException | ClassNotFoundException e) {
            log.error("服务调用发生错误: ", e);
            throw new RPCException("服务调用失败: ", e);
        }
    }
}
