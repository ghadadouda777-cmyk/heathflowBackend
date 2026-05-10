package com.example.backendhealth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  // Paths that are publicly accessible — no JWT required
  private static final List<String> PUBLIC_PREFIXES = List.of(
    "/api/auth/",
    "/api/patients/",
    "/api/plans-alimentaires/",
    "/api/regimes-alimentaires/",
    "/api/repas/",
    "/api/rendez-vous/",
    "/api/consultations/",
    "/api/conversations/",
    "/api/messages/",
    "/api/plans-exercices/"
  );

  private boolean isPublic(HttpServletRequest request) {
    String path = request.getRequestURI();
    return PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException {

    // Skip JWT processing for public endpoints
    if (isPublic(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");
    String token = null;
    String email = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      try {
        email = jwtUtil.extractEmail(token);
        System.out.println("🔑 JWT Filter: Token extracted, email = " + email);
      } catch (Exception e) {
        System.out.println("❌ JWT Filter: Failed to extract email - " + e.getMessage());
        email = null;
      }
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (jwtUtil.validateToken(token, userDetails.getUsername())) {
          UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
          System.out.println("✅ JWT Filter: Authentication set for user: " + email);
        } else {
          System.out.println("❌ JWT Filter: Token validation failed for " + email);
        }
      } catch (Exception e) {
        System.out.println("❌ JWT Filter: Error loading user - " + e.getMessage());
        e.printStackTrace();
      }
    }

    filterChain.doFilter(request, response);
  }
}
