package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
public class AppUserRowMapper implements RowMapper<AppUser> {
    @Override
    public AppUser mapRow(ResultSet resultSet, int i) throws SQLException {
        String rolesStr = resultSet.getString("roles");
        return AppUser.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("username"))
                .email(resultSet.getString("email"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .passwd(resultSet.getString("passwd"))
                .approvedAt(resultSet.getObject("approved_at", OffsetDateTime.class))
                .roles(rolesStr == null ? Collections.emptyList(): Arrays.asList(rolesStr.split(",")))
                .build();
    }
}
