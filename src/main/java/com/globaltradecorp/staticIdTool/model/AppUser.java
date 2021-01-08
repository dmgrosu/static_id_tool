package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Value;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Value
@Builder
public class AppUser {
    Long id;
    String username;
    String passwd;
    String firstName;
    String lastName;
    String email;

    public boolean isNew() {
        return id == null;
    }
}
