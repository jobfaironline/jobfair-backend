package org.capstone.job_fair.config.jwt;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    @Value("${jwt.refresh-expiration}")
    private long JWT_REFRESH_EXPIRATION;

    public String generateToken(String email, long expiredLength){
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredLength * 60 * 1000 * 1000);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


    public String generateToken(Authentication authentication, long expiredLength){
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredLength * 60 * 1000 * 1000);

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


    public String generateToken(Authentication authen) {
        return generateToken(authen, JWT_EXPIRATION);
    }

    public String generateToken(String email) {
        return generateToken(email, JWT_EXPIRATION);
    }

    public String generateRefreshToken(Authentication authentication){
        return generateToken(authentication, JWT_REFRESH_EXPIRATION);
    }
    public String generateRefreshToken(String email){
        return generateToken(email, JWT_REFRESH_EXPIRATION);
    }



    public String getUsernameFromJwt(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
        }
    }
