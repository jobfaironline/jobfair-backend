package org.capstone.job_fair.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.config.jwt.details.UserDetailsServiceImpl;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        //get authorization header
        String authorizationHeader = request.getHeader("Authorization");
        //check if header exist and has jwt token prefix ("Bearer ") ?
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //get jwt from request
            String jwt = getJwtFromRequest(request);
            //validate token if it exists
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                //get email from jwt
                String email = tokenProvider.getUsernameFromJwt(jwt);
                //get userDetails too
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null) {
                    //Check if user is deactivated
                    if (((UserDetailsImpl) userDetails).getStatus().equals(AccountStatus.INACTIVE)){
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        //TODO: add message to response
                        return;
                    }
                    //if userDetails is valid, set data into Security Context
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("Failed on set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }
}
