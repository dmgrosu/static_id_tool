package com.globaltradecorp.staticIdTool.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
public class ComponentRowMapper implements RowMapper<Map.Entry<Integer, String>> {
    @Override
    public Map.Entry<Integer, String> mapRow(ResultSet resultSet, int i) throws SQLException {
        return new AbstractMap.SimpleEntry<>(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
