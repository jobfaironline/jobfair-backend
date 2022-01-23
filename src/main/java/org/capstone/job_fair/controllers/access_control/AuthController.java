package org.capstone.job_fair.controllers.access_control;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.config.jwt.JwtTokenProvider;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.controllers.payload.requests.LoginRequest;
import org.capstone.job_fair.controllers.payload.responses.LoginResponse;
import org.capstone.job_fair.controllers.payload.requests.RefreshTokenRequest;
import org.capstone.job_fair.controllers.payload.responses.RefreshTokenResponse;
import lombok.AllArgsConstructor;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authManager;

    private final JwtTokenProvider tokenProvider;

    private final AccountService accountService;

    private final CompanyEmployeeService companyEmployeeService;

    private boolean isAccountHasRole(UserDetailsImpl userDetails, Role role){
        return userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.getAuthority()));
    }

    @PostMapping(path = ApiEndPoint.Authentication.LOGIN_ENDPOINT)
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
            String refreshToken = tokenProvider.generateRefreshToken(authentication);
            //get user principle from authentication obj
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            //check if the Company Employee first time login with provided password
            boolean isEmployeeFirstTime = false;
            if(isAccountHasRole(userDetails, Role.COMPANY_EMPLOYEE) && userDetails.getStatus().equals(AccountStatus.REGISTERED)) {
                companyEmployeeService.updateEmployeeStatus(userDetails.getEmail());
                isEmployeeFirstTime = true;
            }

            //get list of roles
            String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());
            //then return login response which include email and password in userDetails
            LoginResponse response = new LoginResponse(
                    userDetails.getEmail(),
                    userDetails.getPassword(),
                    userDetails.getStatus(),
                    role,
                    jwt,
                    refreshToken,
                    isEmployeeFirstTime
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path = ApiEndPoint.Authentication.REFRESH_TOKEN_ENDPOINT)
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest tokenRequest) {
        if (!tokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            log.info("Invalid refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageUtil.getMessage(MessageConstant.AccessControlMessage.INVALID_REFRESH_TOKEN));
        }
        final String email = tokenProvider.getUsernameFromJwt(tokenRequest.getRefreshToken());
        Optional<AccountEntity> accountOptional = accountService.getActiveAccountByEmail(email);
        if (!accountOptional.isPresent()) {
            log.info("Token claim is invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageUtil.getMessage(MessageConstant.AccessControlMessage.TOKEN_CLAIM_INVALID));
        }
        final AccountEntity account = accountOptional.get();
        String newToken = tokenProvider.generateToken(account.getEmail());
        String newRefreshToken = tokenProvider.generateRefreshToken(account.getEmail());
        RefreshTokenResponse response = new RefreshTokenResponse(newRefreshToken, newToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
