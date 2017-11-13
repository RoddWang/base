/**
 * Copyright (c) 2017, China Mobile IOT All Rights Reserved.
 */
package exception;

import org.springframework.dao.DataAccessException;

/**   
 * 此类描述的是：自定义Dao数据访问异常   
 * @author: wangjian  
 * @date: 2017年11月13日 上午11:50:39    
 */
public class ApiDataAccessException extends DataAccessException {

    /**   
     * serialVersionUID:
     *   
     * @since Ver 1.1   
     */   
    private static final long serialVersionUID = -1945327732559868037L;

    /**   
     * 创建一个新的实例 ApiDataAccessException.   
     *   
     * @param msg
     * @param cause   
     */
    public ApiDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**   
     * 创建一个新的实例 ApiDataAccessException.   
     *   
     * @param msg   
     */
    public ApiDataAccessException(String msg) {
        super(msg);
    }

    
}
