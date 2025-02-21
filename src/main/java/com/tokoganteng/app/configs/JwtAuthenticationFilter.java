package com.tokoganteng.app.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokoganteng.app.constants.AuthConstant;
import com.tokoganteng.app.dto.response.BaseResponse;
import com.tokoganteng.app.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${auth.secret}")
    private String SECRET;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response); // OK jika tidak ada header otorisasi
                return;
            }

            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) { // Perbaikan di sini
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    Claims claims = Jwts.parser()
                            .setSigningKey(SECRET)
                            .parseClaimsJws(jwt)
                            .getBody();
                    String userId = claims.get(AuthConstant.HEADER_X_WHO, String.class);
                    request.setAttribute(AuthConstant.HEADER_X_WHO, userId);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response); // Dipindahkan ke dalam blok if isTokenValid
                } else { // Jika token invalid
                    createHttpUnAuthentication(response, "Invalid Token"); // Panggil method ini
                }
            } else if (userEmail == null) {
                createHttpUnAuthentication(response, "Invalid Token");
            } else {
                filterChain.doFilter(request, response);
            }

        } catch (Exception ex) {
            createHttpUnAuthentication(response, ex.getMessage());
        }
    }

    private void createHttpUnAuthentication(HttpServletResponse httpServletResponse, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BaseResponse response = BaseResponse.builder()
                .success(false)
                .message(message)
                .build();
        String json = mapper.writeValueAsString(response);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().write(json);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

}

