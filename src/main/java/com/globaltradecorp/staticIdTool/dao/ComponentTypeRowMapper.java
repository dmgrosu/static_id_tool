package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.ComponentType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
public class ComponentTypeRowMapper implements RowMapper<ComponentType> {
    @Override
    public ComponentType mapRow(ResultSet resultSet, int i) throws SQLException {
        return ComponentType.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
