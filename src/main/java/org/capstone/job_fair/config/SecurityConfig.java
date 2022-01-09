package org.capstone.job_fair.config;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.jwt.JwtAuthEntryPoint;
import org.capstone.job_fair.jwt.JwtAuthenticationFilter;
import org.capstone.job_fair.jwt.JwtTokenProvider;
import org.capstone.job_fair.jwt.details.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthEntryPoint unauthorizedHandler;

    private final JwtTokenProvider tokenProvider;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder for encoding password
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService) // provide account service for spring security
                .passwordEncoder(passwordEncoder()); // and password encoder
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/webjars/**",
                "/csrf",
                "/error",
                "/swagger-ui/**"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() //handle exception
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //Authenticated API Security
                .authorizeRequests().antMatchers(ApiEndPoint.Authentication.AUTHENTICATION_ENDPOINT + "/**").permitAll().and()
                //Account API Security
                .authorizeRequests().antMatchers(ApiEndPoint.Attendant.ACCOUNT_ENDPOINT + "/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();

        //after logout success, invalidate session
        http.logout().logoutUrl(ApiEndPoint.Authentication.LOGOUT_ENDPOINT).invalidateHttpSession(true);
        //adding a filter to validate jwt token
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
