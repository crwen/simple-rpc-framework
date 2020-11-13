package top.crwenassert.rpc.server.dto;

import lombok.Data;
import top.crwenassert.rpc.server.enums.ResponseCode;

import java.io.Serializable;

/**
 * ClassName: RPCResponse
 * Description:
 * date: 2020/11/13 13:59
 *
 * @author crwen
 * @create 2020-11-13-13:59
 * @since JDK 1.8
 */
@Data
public class RPCResponse<T> implements Serializable {

    private static final long serialVersionUID = 55166229326205919L;
    /**
     *  响应状态码
     */
    private Integer statusCode;

    /**
     *  响应补充信息
     */
    private String message;

    /**
     *  响应数据
     */
    private T data;

    public static <T> RPCResponse<T> success(T data) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RPCResponse<T> fail(ResponseCode code) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
