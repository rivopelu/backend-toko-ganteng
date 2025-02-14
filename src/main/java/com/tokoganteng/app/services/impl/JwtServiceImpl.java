package com.tokoganteng.app.services.impl;


import com.tokoganteng.app.constants.AuthConstant;
import com.tokoganteng.app.entities.Account;
import com.tokoganteng.app.exception.NotFoundException;
import com.tokoganteng.app.repositories.AccountRepository;
import com.tokoganteng.app.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.tokoganteng.app.constants.AuthConstant.HEADER_X_WHO;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final AuthConstant authConstant = new AuthConstant();
    private final AccountRepository accountRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(UserDetails userDetails) {

        Optional<Account> account = accountRepository.findByEmailAndActiveIsTrue(userDetails.getUsername());
        if (account.isEmpty()) {
            throw new NotFoundException("Account Not Found");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(HEADER_X_WHO, account.get().getId());
        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extractClaims).setSubject(userDetails.getUsername()).setExpiration(new Date(System.currentTimeMillis() + authConstant.getEXPIRATION_TIME())).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(authConstant.getSECRET());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }
}

