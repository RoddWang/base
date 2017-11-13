package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 此类描述的是：获取计算机名
 * 
 * @author: wangjian
 * @date: 2017年11月13日 下午2:18:57
 */
public final class OSUtils {

    /**
     * 私有化工具类，禁止实例化 OSUtils.
     * 
     */
    private OSUtils() {
        throw new IllegalAccessError("禁止实例化工具类 OSUtils");
    }

    /**
     * 
     * 获取计算机名win-linux都可以
     * 
     * @return
     * @author wangjian
     * @date 2017年11月13日
     */
    public static String getHostNameForLiunx() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }

}
