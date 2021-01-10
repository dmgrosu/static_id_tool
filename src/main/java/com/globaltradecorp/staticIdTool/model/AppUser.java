package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
    List<String> roles = new ArrayList<>();

    public boolean isNew() {
        return id == null;
    }

    public boolean isApproved() {
        return approvedAt != null;
    }
}
