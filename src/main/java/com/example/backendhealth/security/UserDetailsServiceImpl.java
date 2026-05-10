package com.example.backendhealth.security;

import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        user u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User non trouvé : " + email));

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPwd(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole()))
        );
    }
}