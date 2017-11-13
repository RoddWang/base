/**
 * Copyright (c) 2017, China Mobile IOT All Rights Reserved.
 */
package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import exception.ApiDataAccessException;

/**
 * 此类描述的是： 处理JdbcTempate 查询对象报空的异常
 * 
 * @author: wangjian
 * @date: 2017年11月13日 上午11:42:00
 */
@Component
public class NullableJdbcTemplate extends JdbcTemplate {

    /**
     * 
     * 构造方法
     * 
     * @param dataSource
     */
    public NullableJdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        try {
            return super.queryForObject(sql, rowMapper, args);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiDataAccessException(e.getMessage());
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
        try {
            return super.queryForObject(sql, args, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiDataAccessException(e.getMessage());
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
        try {
            return super.queryForObject(sql, args, argTypes, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiDataAccessException(e.getMessage());
        }
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        try {
            return super.queryForObject(sql, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiDataAccessException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object... args) {
        try {
            Map<String, Object> resultMap = super.queryForMap(sql, args);
            if (null == resultMap) {
                return new HashMap<>();
            } else {
                return resultMap;
            }
        } catch (EmptyResultDataAccessException e) {
            List<Map<String, Object>> list = super.queryForList(sql, args);
            return list.get(0);
        }
    }
}
