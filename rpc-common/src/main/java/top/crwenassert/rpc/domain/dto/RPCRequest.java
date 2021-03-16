package top.crwenassert.rpc.domain.dto;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: RPCRequest
 * Description: 消费者与提供者之间传输的请求对象
 * date: 2020/11/13 13:20
 *
 * @author crwen
 * @create 2020-11-13-13:20
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RPCRequest implements Serializable {

    private static final long serialVersionUID = 1832953511510140746L;

    /**
     *  请求号
     */
    private String requestId;

    /**
     *  待调用的接口名称
     */
    private String interfaceName;

    /**
     *  待调用的方法名称
     */
    private String methodName;

    /**
     * 待调用方法的参数
     */
    private Object[] parameters;

    /**
     * 待调用方法的参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;

    /**
     * 所属组
     */
    private String group;

    public String toRpcServiceName() {
        if (Strings.isNullOrEmpty(this.group))
            return this.interfaceName;
        return this.getInterfaceName() + "_" + this.getGroup();
    }
}
