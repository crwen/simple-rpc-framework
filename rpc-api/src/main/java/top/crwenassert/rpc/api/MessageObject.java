package top.crwenassert.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: MessageObject
 * Description:
 * date: 2020/11/13 13:28
 *
 * @author crwen
 * @create 2020-11-13-13:28
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageObject implements Serializable {

    private Integer id;
    private String message;

}
