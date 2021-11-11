package com.my.hmc.security.jwt;

import com.my.hmc.domain.etype.UserRole;
import com.my.hmc.security.UserDetailsImpl;
import com.my.hmc.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtUtils {

    private final UserDetailsServiceImpl userDetailsService;
    private final String JWT_SECRET = "secret";
    private final SignatureAlgorithm SIGNATURE = SignatureAlgorithm.HS256;

    public String createToken(String username) {

        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SIGNATURE, JWT_SECRET)
                .compact();
    }

    public boolean isValidToken(String token){
        Claims claims = getAllClaims(token);

        Date expiration = claims.getExpiration();
        String username = String.valueOf(claims.get("username"));

        return expiration.after(new Date()) && userDetailsService.loadUserByUsername(username) != null;
    }

    public TokenDto getUserInfo(String token) {
        Claims claims = getAllClaims(token);
        return new TokenDto(claims.get("username").toString());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

}
