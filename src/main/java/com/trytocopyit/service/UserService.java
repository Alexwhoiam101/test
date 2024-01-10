package com.trytocopyit.service;

import com.trytocopyit.entity.Acc;
import com.trytocopyit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //private AuthenticationManager authenticationManager;

    public static final int MAX_FAILED_ATTEMPTS = 3;

    public void increaseFailedAttempts(Acc user) {
        user.setFailedAttempt(user.getFailedAttempt()+1);
        if(user.getFailedAttempt() >= MAX_FAILED_ATTEMPTS)
            lock(user);
        userRepository.save(user);
    }

    public void resetFailedAttempts(String name) {
        Acc user = userRepository.findAccount(name);
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    public void lock(Acc user) {
        user.setActive(false);
        userRepository.save(user);
    }

    public void save(Acc user){
        userRepository.save(user);
        return;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Acc account = userRepository.findAccount(username);
        System.out.println("Account= " + account);

        if (account == null) {
            throw new UsernameNotFoundException("User "
                    + username + " was not found in the database");
        }

        String role = account.getUserRole();

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();

        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        grantList.add(authority);

        boolean enabled = account.isActive();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        UserDetails userDetails = (UserDetails) new User(account.getUserName(),
                account.getEncrytedPassword(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, grantList);

        return userDetails;
    }

    public void autoLogin(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        //authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            System.out.println(String.format("Auto login %s successfully!", username));
        }
    }

    public void logout(){
        SecurityContextHolder.clearContext();
    }
}