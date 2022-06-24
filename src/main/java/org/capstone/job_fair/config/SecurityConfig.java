package org.capstone.job_fair.config;

import lombok.AllArgsConstructor;
import org.capstone.job_fair.config.jwt.JwtAuthEntryPoint;
import org.capstone.job_fair.config.jwt.JwtAuthenticationFilter;
import org.capstone.job_fair.config.jwt.JwtTokenProvider;
import org.capstone.job_fair.config.jwt.details.UserDetailsServiceImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

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
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService) // provide account service for spring security
                .passwordEncoder(passwordEncoder()); // and password encoder
    }

    @Override
    public void configure(WebSecurity web) {
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
                .cors().configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.applyPermitDefaultValues();
                    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
                    return configuration;
                }).and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() //handle exception
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //Authenticated API Security
                .authorizeRequests().antMatchers(ApiEndPoint.Authentication.AUTHENTICATION_ENDPOINT + "/**").permitAll().and()
                //Attendant API Security
                .authorizeRequests().antMatchers(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "/{attendantId}").permitAll().and()
                //Account API Security
                .authorizeRequests().antMatchers(ApiEndPoint.Attendant.REGISTER_ENDPOINT).permitAll().and()
                //Get general info for searching email in forgot password
                .authorizeRequests().antMatchers(ApiEndPoint.Account.GET_GENERAL_INFO).permitAll().and()
                .authorizeRequests().antMatchers(ApiEndPoint.CompanyEmployee.REGISTER_COMPANY_MANAGER).permitAll().and()
                //ResetPassword API Security
                .authorizeRequests().antMatchers(ApiEndPoint.Authentication.REFRESH_TOKEN_ENDPOINT).permitAll().and()
                .authorizeRequests().antMatchers(ApiEndPoint.Authentication.GENERATE_OTP_ENDPOINT).permitAll().and()
                //Company API Security: specific end point will be configured inside controller
                .authorizeRequests().antMatchers(ApiEndPoint.Company.COMPANY_ENDPOINT).permitAll().and()
                //Attendant API Security: specific end point will be configured inside controller
                .authorizeRequests().antMatchers(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT).permitAll().and()
                .authorizeRequests().antMatchers(ApiEndPoint.Authorization.VERIFY_USER + "/**").permitAll().and()
                .authorizeRequests().antMatchers(ApiEndPoint.Authorization.NEW_VERIFY_LINK + "/**").permitAll().and()
                //Job Fair API Security: specific end point for get job fair detail
                .authorizeRequests().antMatchers(ApiEndPoint.JobFair.JOB_FAIR + "/**").permitAll().and()
                //Attendant API Security: specific end point will be configured inside controller
                .authorizeRequests().antMatchers(ApiEndPoint.CompanyEmployee.REGISTER_COMPANY_MANAGER).permitAll()
                .anyRequest().authenticated().and()
                .headers().contentSecurityPolicy("script-src 'self'");

        //after logout success, invalidate session
        http.logout().logoutUrl(ApiEndPoint.Authentication.LOGOUT_ENDPOINT).invalidateHttpSession(true);
        //adding a filter to validate jwt token
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
