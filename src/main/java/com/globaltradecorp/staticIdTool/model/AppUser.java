package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Value
@Builder
public class AppUser {
    Integer id;
    String username;
    String passwd;
    String firstName;
    String lastName;
    String email;
    @Nullable
    OffsetDateTime approvedAt;
    @Builder.Default
    List<Role> roles = new ArrayList<>();

    public boolean isNew() {
        return id == null;
    }

    public boolean isApproved() {
        return approvedAt != null;
    }

    public String getRolesString() {
        if (roles.isEmpty()) {
            return "";
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.joining(","));
    }

    public String getApprovedFormatted() {
        return isApproved() ? approvedAt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) : "<not approved>";
    }

    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }
}
