package top.crwenassert.rpc.service;

import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.transport.socket.server.SocketServer;

/**
 * ClassName: TestServer
 * Description:
 * date: 2020/11/13 13:41
 *
 * @author crwen
 * @create 2020-11-13-13:41
 * @since JDK 1.8
 */
public class TestServer {
    public static void main(String[] args) {
        SocketServer rpcServer = new SocketServer("127.0.0.1", 9999, CommonSerializer.JSON_SERIALIZER);
        rpcServer.start();
    }
}
