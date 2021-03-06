package top.crwenassert.rpc.domain.dto;

import lombok.*;
import top.crwenassert.rpc.domain.enums.CacheCode;

import java.io.Serializable;

/**
 * ClassName: Invocation
 * Description:
 * date: 2021/3/5 21:20
 *
 * @author crwen
 * @create 2021-03-05-21:20
 * @since JDK 1.8
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invocation implements Serializable {

    private static final long serialVersionUID = 1166708100589382782L;


    private CacheCode cacheCode;

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

    public String toNameString() {
        StringBuffer res = new StringBuffer();
        res.append(methodName);
        res.append("(");
        for (int i = 0; i < paramTypes.length; i++) {
            res.append(paramTypes[i].getCanonicalName());
            res.append(";");
        }
        res.append(")");
        return res.toString();
    }

    public String toFullString() {
        StringBuffer res = new StringBuffer();
        res.append(methodName);
        res.append("(");
        for (int i = 0; i < paramTypes.length; i++) {
            res.append(paramTypes[i].getCanonicalName());
            res.append(":");
            res.append(parameters[i]);
            res.append(";");
        }
        res.append(")");
        return res.toString();
    }

}
