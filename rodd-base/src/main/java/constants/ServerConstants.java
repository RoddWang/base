package constants;

/**
 * 此类描述的是：Server 常量类
 * 
 * @author: wangjian
 * @date: 2017年11月13日 下午2:07:41
 */
public final class ServerConstants {
    /**
     * server的常量
     */
    public static final String CHARSET = "UTF-8";

    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String RESULT = "result";
    public static final String RESULT_CODE = "resultCode";
    public static final String SUCCESS = "0";
    public static final String OK = "正确";


    /**
     * 私有化该实例 防止被实例化 ServerConstants.
     **/

    private ServerConstants() {
        throw new IllegalArgumentException("禁止实例化常量类 ServerConstants");
    }
}
