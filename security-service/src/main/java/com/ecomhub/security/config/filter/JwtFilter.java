package com.ecomhub.security.config.filter;

import com.ecomhub.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${security.database.validation}")
    private boolean userDetailsServiceEnabled;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Stop execution if no valid token is found
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUserName(token);
            List<GrantedAuthority> role = jwtService.extractRoles(token);

            // If the user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authToken;

                // Case 1: Use UserDetailsService if it's present (e.g., user-service context)
                if(userDetailsServiceEnabled) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Validate token using user details
                    if(jwtService.validateToken(token, userDetails)){
                        authToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid or expired token");
                        return; // Prevents further execution
                    }
                } else { // Case 2: No UserDetailsService → Use direct extraction via JWT token (e.g., product-service)
                    authToken = new UsernamePasswordAuthenticationToken(username, null, role);
                }
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return; // Prevents further execution
        }
        filterChain.doFilter(request, response);
    }
}
