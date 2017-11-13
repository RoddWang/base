/**
 * Copyright (c) 2017, China Mobile IOT All Rights Reserved.
 */
package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * 此类描述的是： OSUtils 测试类
 * 
 * @author: wangjian
 * @date: 2017年11月13日 下午2:46:23
 */
public class OSUtilsTest {

    public static final Logger logger = LoggerFactory.getLogger(OSUtilsTest.class);

    @Test
    public void getHostNameForLiunx() {
        logger.debug("该操作系统主机名为： {}",OSUtils.getHostNameForLiunx());
    }
}
