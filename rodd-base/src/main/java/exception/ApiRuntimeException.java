package exception;

/**
 * 此类描述的是：
 * 
 * @author: wangjian
 * @date: 2017年11月13日 上午11:25:04
 */
public class ApiRuntimeException extends RuntimeException {

    /**
     * serialVersionUID:
     * 
     * @since Ver 1.1
     */
    private static final long serialVersionUID = 2891235102881319035L;

    /**
     * 创建一个新的实例 ApiRuntimeException.
     * 
     */
    public ApiRuntimeException() {
        super();
    }

    /**
     * 创建一个新的实例 ApiRuntimeException.
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ApiRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 创建一个新的实例 ApiRuntimeException.
     * 
     * @param message
     * @param cause
     */
    public ApiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 创建一个新的实例 ApiRuntimeException.
     * 
     * @param message
     */
    public ApiRuntimeException(String message) {
        super(message);
    }

    /**
     * 创建一个新的实例 ApiRuntimeException.
     * 
     * @param cause
     */
    public ApiRuntimeException(Throwable cause) {
        super(cause);
    }


}
