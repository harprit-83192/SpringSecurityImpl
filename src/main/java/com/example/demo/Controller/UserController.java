package com.example.demo.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        if(user.getEmail() == null && user.getUsername() == null && user.getPassword() == null){
            return ResponseEntity.badRequest().body("Bad Request!");
        }

        User newUser = userService.registerUser(user);
        if(newUser != null){
            return ResponseEntity.ok(newUser);
        }
        return ResponseEntity.internalServerError().body("Something wrong with Request!");
    }

    @PostMapping("/login/{username}/{password}")
    public ResponseEntity<?> loginUser(@PathVariable String username, @PathVariable String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username,password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok("Token: "+token);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token){
        if(token == null){
            return ResponseEntity.badRequest().body("Token provided is null!");
        }
        boolean isTokenValid = jwtUtil.validateToken(token);
        return ResponseEntity.ok("Token Valid: "+isTokenValid);
    }
}
