package top.crwenassert.rpc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * ClassName: PropertiesFileUtil
 * Description:
 * date: 2021/3/15 20:00
 *
 * @author crwen
 * @create 2021-03-15-20:00
 * @since JDK 1.8
 */
@Slf4j
public class PropertiesFileUtil {
    private PropertiesFileUtil(){}

    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        String rpcConfigPath = "";
        if (url != null) {
            rpcConfigPath = url.getPath();
        }
        Properties properties = null;
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(rpcConfigPath), StandardCharsets.UTF_8)){
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            log.error("读取配置文件发生错误：{}", fileName);
        }
        return properties;
    }
}
