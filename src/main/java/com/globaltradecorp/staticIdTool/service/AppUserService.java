package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.UserDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Service
@AllArgsConstructor
public class AppUserService {

    private final UserDao userDao;

    public void registerNewUser(AppUser appUser) {
        appUser.addRole(Role.USER);
        userDao.saveUser(appUser);
    }

    public boolean usernameExists(String username) {
        return userDao.usernameExists(username);
    }

    public boolean userEmailExists(String email) {
        return userDao.userEmailExists(email);
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

    public List<AppUser> getAllUsers() {
        return userDao.getAll();
    }

    public void approveUser(Integer userId, OffsetDateTime userTime) {
        userDao.approveUser(userId, userTime);
    }

    public void deleteUser(Integer userId, OffsetDateTime userTime) {
        userDao.deleteUser(userId, userTime);
    }

    public void blockUser(Integer userId) {
        userDao.approveUser(userId, null);
    }
}
