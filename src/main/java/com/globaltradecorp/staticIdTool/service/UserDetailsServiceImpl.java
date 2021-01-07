package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.UserDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> optionalAppUser = userDao.findByUsername(username);
        if (!optionalAppUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        AppUser appUser = optionalAppUser.get();
        return new User(appUser.getUsername(), appUser.getPasswd(), getGrantedAuthorities());

    }

    private List<GrantedAuthority> getGrantedAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("User"));
    }

}
