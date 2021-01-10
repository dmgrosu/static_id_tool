package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;

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

    public boolean isNew() {
        return id == null;
    }

    public boolean isApproved() {
        return approvedAt != null;
    }
}
