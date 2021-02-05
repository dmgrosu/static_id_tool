package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
public class AppUserRowMapper implements RowMapper<AppUser> {
    @Override
    public AppUser mapRow(ResultSet resultSet, int i) throws SQLException {
        return AppUser.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("username"))
                .email(resultSet.getString("email"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .passwd(resultSet.getString("passwd"))
                .approvedAt(resultSet.getObject("approved_at", OffsetDateTime.class))
                .roles(rolesFromString(resultSet.getString("roles")))
                .build();
    }

    private List<Role> rolesFromString(String rolesStr) {
        if (!StringUtils.hasText(rolesStr)) {
            return Collections.emptyList();
        }
        return Arrays.stream(rolesStr.split(","))
                .map(Role::valueOf)
                .collect(Collectors.toList());
    }
}
