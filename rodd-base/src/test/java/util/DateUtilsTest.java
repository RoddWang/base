package util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 此类描述的是： 日期工具类的测试类
 * 
 * @author: wangjian
 * @date: 2017年11月13日 下午2:45:38
 */
public class DateUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(DateUtilsTest.class);

    @Test(dataProvider = "getParam")
    public void dateTest(String dateStr, String format, Date date1, String format1, String format2, Date date3) {
        logger.debug("parseString: {}", DateUtils.parseString(dateStr, format));
        logger.debug("formatDate: {}", DateUtils.formatDate(date1, format1));
        logger.debug("getCurrentTimestamp: {}", DateUtils.getCurrentTimestamp(format2));
        logger.debug("getResidualDaysOfMonth: {}", DateUtils.getResidualDaysOfMonth(date3));
        logger.debug("getCurDateForString: {}", DateUtils.getCurDateForString());
    }

    @DataProvider
    public Object[][] getParam() {
        return new Object[][] {
            new Object[] { "2017-11-13 13:25", DateUtils.DATETIME_YYYY_MM_DD_HH_MM, DateUtils.getTodayOfLastMonth(),
                DateUtils.DATE_YY_MM_DD, DateUtils.DATETIME_YYYY_MM_DD_HH_MM_SS_SSS, DateUtils.getYesterday() } };
    }
}
