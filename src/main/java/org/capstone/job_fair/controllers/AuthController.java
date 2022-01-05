package org.capstone.job_fair.controllers;

import org.capstone.job_fair.jwt.JwtTokenProvider;
import org.capstone.job_fair.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.payload.LoginRequest;
import org.capstone.job_fair.payload.LoginResponse;
import org.capstone.job_fair.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/auth")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;

    private final JwtTokenProvider tokenProvider;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Validated @RequestBody LoginRequest request) {
        //initialize UsernameAndPasswordAuthenticationToken obj
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        //let authentication Manager authenticate this obj
        Authentication authentication = authManager.authenticate(authToken);
        if (authentication.isAuthenticated()) {
            //if it's authenticated => set them to security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //then generate jwt token to return client
            String jwt = tokenProvider.generateToken(authentication);
            //get user principle from authentication obj
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            //get list of roles
            List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
            //then return login response which include email and password in userDetails
            LoginResponse response = new LoginResponse(
                    userDetails.getEmail(),
                    userDetails.getPassword(),
                    userDetails.getStatus(),
                    roles,
                    jwt);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
    }
}
