package com.globaltradecorp.staticIdTool.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Data
@Builder
public class AppUser {
    private Long id;
    private String username;
    private String passwd;
    private String firstName;
    private String lastName;
    private String email;

    public boolean isNew() {
        return id == null;
    }
}
