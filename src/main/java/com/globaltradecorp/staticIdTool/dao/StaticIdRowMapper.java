package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.StaticId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
public class StaticIdRowMapper implements RowMapper<StaticId> {
    @Override
    public StaticId mapRow(ResultSet resultSet, int i) throws SQLException {
        return StaticId.builder()
                .id(resultSet.getLong("id"))
                .value(resultSet.getString("id_value"))
                .componentName(resultSet.getString("component_name"))
                .createdBy(AppUser.builder()
                        .id(resultSet.getLong("user_id"))
                        .username(resultSet.getString("username"))
                        .firstName(resultSet.getString("user_first_name"))
                        .lastName(resultSet.getString("user_last_name"))
                        .email(resultSet.getString("user_email"))
                        .build())
                .createdAt(resultSet.getObject("created_at", OffsetDateTime.class))
                .build();
    }
}
