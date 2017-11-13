package constants;

import exception.ApiRuntimeException;

/**
 * 此类描述的是： 错误码常量
 * 
 * @author: wangjian
 * @date: 2017年11月13日 下午2:33:36
 */
public final class ErrorCodeConstant {

    /**
     * 私有化该实例 防止被实例化 ErrorCodeConstant.
     **/

    private ErrorCodeConstant() {
        throw new ApiRuntimeException("禁止实例化常量类 ErrorCodeConstant");
    }
}
