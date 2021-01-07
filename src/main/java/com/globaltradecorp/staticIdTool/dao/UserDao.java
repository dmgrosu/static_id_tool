package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.AppUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Component
public class UserDao {
    public Optional<AppUser> findByUsername(String username) {
        // TODO replace this stub with database read implementation
        return Optional.of(AppUser.builder()
                .username("jdoe")
                .password("passwd")
                .build());
    }
}
