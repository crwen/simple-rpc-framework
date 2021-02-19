package top.crwenassert.rpc.transport.socket.client;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.RPCClient;
import top.crwenassert.rpc.domain.dto.RPCRequest;
import top.crwenassert.rpc.domain.dto.RPCResponse;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.domain.enums.ResponseCode;
import top.crwenassert.rpc.exception.RPCException;
import top.crwenassert.rpc.registry.NacosServiceDiscovery;
import top.crwenassert.rpc.registry.ServiceDiscovery;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.transport.socket.util.ObjectReader;
import top.crwenassert.rpc.transport.socket.util.ObjectWriter;
import top.crwenassert.rpc.util.RPCMessageChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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

    private final ServiceDiscovery serviceDiscovery;
    private CommonSerializer serializer;

    public SocketClient() {
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    @Override
    public Object sendRequest(RPCRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCErrorEnum.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            RPCResponse rpcResponse = (RPCResponse) ObjectReader.readObject(inputStream);
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
            RPCMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        } catch (IOException  e) {
            log.error("服务调用发生错误: ", e);
            throw new RPCException("服务调用失败: ", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
