//package com.example.QuoraBackend.security.service;
//
//import com.example.QuoraBackend.dto.request.LoginRequest;
//import com.example.QuoraBackend.dto.request.RegisterRequest;
//import com.example.QuoraBackend.dto.response.RegisterResponse;
//import com.example.QuoraBackend.model.Role;
//import com.example.QuoraBackend.model.User;
//import com.example.QuoraBackend.repository.RoleRepository;
//import com.example.QuoraBackend.repository.UserRepository;
//import com.example.QuoraBackend.security.service.JwtService;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class AuthService {
//
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//
//    public AuthService(
//            AuthenticationManager authenticationManager,
//            JwtService jwtService,
//            UserRepository userRepository,
//            RoleRepository roleRepository,
//            PasswordEncoder passwordEncoder
//    ) {
//        this.authenticationManager = authenticationManager;
//        this.jwtService = jwtService;
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//
//    // login
//    // Authenticates credentials and issues JWT
//
//    public String login(LoginRequest request) {
//
//        Authentication authentication =
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                request.getEmail(),
//                                request.getPassword()
//                        )
//                );
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return jwtService.generateToken(userDetails);
//    }
//
//    // register
//    // Creates user with default ROLE_USER
//
////    public RegisterResponse register(RegisterRequest request) {
////
////        User user = User.builder()
////                .username(request.getUsername())
////                .email(request.getEmail())
////                .password(passwordEncoder.encode(request.getPassword()))
////
////                .build();
////
////        userRepository.save(user);
////
////        String jwt = jwtService.generateToken((UserDetails) user);
////
////        return new RegisterResponse(
////                "Welcome to the world of questions and answers — where curiosity becomes knowledge ",
////                jwt
////        );
////    }
//
//}
//

package com.example.QuoraBackend.security.service;

import com.example.QuoraBackend.dto.request.LoginRequest;
import com.example.QuoraBackend.dto.request.RegisterRequest;
import com.example.QuoraBackend.dto.response.RegisterResponse;
import com.example.QuoraBackend.exception.ResourceNotFoundException;
import com.example.QuoraBackend.model.Role;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.repository.RoleRepository;
import com.example.QuoraBackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;


    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    // login
    // Authenticates user credentials and returns JWT
    public String login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtService.generateToken(userDetails);
    }

    // register
    // Creates a new user with default ROLE_USER
public RegisterResponse register(RegisterRequest request) {

    Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRoles(Set.of(userRole));   // assign default role
    user.setBio(null);                 // optional

    userRepository.save(user);

    return new RegisterResponse(
            "Welcome " + user.getUsername()
                    + "! Your account has been created successfully. Please log in to continue"
    );
}

}
