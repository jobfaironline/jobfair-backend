package org.capstone.job_fair.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsServiceImpl;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    private String getEmailFromCognito(String jwt) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String uri = "https://jobhub-login.auth.ap-southeast-1.amazoncognito.com/oauth2/userInfo";

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "Bearer " + jwt);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, jwt);
            Map<String, String> json = new ObjectMapper().readValue(result.getBody(), HashMap.class);
            return json.get("email");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            if (context.getAuthentication() != null && !(context.getAuthentication().getPrincipal() instanceof UserDetails)) {
                //get email from jwt
                String tokenValue = ((Jwt) context.getAuthentication().getPrincipal()).getTokenValue();
                String email = getEmailFromCognito(tokenValue);
                //get userDetails too
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null) {
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