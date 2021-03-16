package top.crwenassert.rpc.domain.dto;

import com.google.common.base.Strings;
import lombok.*;

/**
 * ClassName: RpcServiceProperties
 * Description:
 * date: 2021/3/6 0:07
 *
 * @author crwen
 * @create 2021-03-06-0:07
 * @since JDK 1.8
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceProperties {


    private String group;
    private String serviceName;

    public String toRpcServiceName() {
        if (Strings.isNullOrEmpty(this.group))
            return this.serviceName;
        return this.getServiceName() + "_" + this.getGroup();
    }
}
