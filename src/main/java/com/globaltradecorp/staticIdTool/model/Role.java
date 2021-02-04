package com.globaltradecorp.staticIdTool.model;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private String name;

    Role(String name) {
        this.name = name;
    }

}
