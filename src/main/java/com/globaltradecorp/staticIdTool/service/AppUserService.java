package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.UserDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Service
public class AppUserService {

    private final UserDao userDao;

    @Autowired
    public AppUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void registerNewUser(AppUser appUser) {
        userDao.saveUser(appUser);
    }

    public Optional<AppUser> getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Optional<AppUser> getByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Nullable
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userDao.findByUsername(currentUsername).orElse(null);
    }

}
