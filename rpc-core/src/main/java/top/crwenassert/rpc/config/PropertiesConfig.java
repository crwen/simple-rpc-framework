package top.crwenassert.rpc.config;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCConfigEnum;
import top.crwenassert.rpc.serializer.CommonSerializer;
import top.crwenassert.rpc.util.PropertiesFileUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * ClassName: PropertiesConfig
 * Description:
 * date: 2021/3/16 0:58
 *
 * @author crwen
 * @create 2021-03-16-0:58
 * @since JDK 1.8
 */
@Slf4j
public final class PropertiesConfig {
    private final static Properties properties  = load();
    private static String NACOS_ADDRESS;
    private static String host;
    private static Integer port;
    private static CommonSerializer serializer;
    private static final int DEFAULT_PORT = 9999;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_SERIALIZER = 0;


    private static Properties load() {
        Properties prop = PropertiesFileUtil.readPropertiesFile(RPCConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        if (prop == null)
            return null;
        String serialization = prop.getProperty(RPCConfigEnum.RPC_SERIALIZER.getPropertyValue());
        if (Strings.isNullOrEmpty(serialization)) {
            serializer = CommonSerializer.getByCode(DEFAULT_SERIALIZER);
        } else {
            serializer = CommonSerializer.getByName(serialization.trim().toUpperCase());
        }
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取主机地址失败 {}", e);
        }
        String value = prop.getProperty(RPCConfigEnum.PORT.getPropertyValue());
        if (Strings.isNullOrEmpty(value)) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(value.trim());
        }
        NACOS_ADDRESS = prop.getProperty(RPCConfigEnum.NACOS_ADDRESS.getPropertyValue());
        return prop;
    }

    public static String getHost(){
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static String getNacosAddress() {
        return NACOS_ADDRESS;
    }

    public static CommonSerializer getSerializer() {
        return serializer;
    }
}
