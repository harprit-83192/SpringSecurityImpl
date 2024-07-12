package com.example.demo.Service;

import com.example.demo.Entity.CustomUserDetails;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User userDetails){
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        return userRepository.save(userDetails);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return new CustomUserDetails(user);
    }
}
