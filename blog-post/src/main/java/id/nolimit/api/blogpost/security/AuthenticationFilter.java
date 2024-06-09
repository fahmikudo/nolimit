package id.nolimit.api.blogpost.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null) {
            if (jwtUtil.validateToken(token) == Boolean.TRUE) {
                try {
                    Claims claims = jwtUtil.getAllClaimsFromToken(token);
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), null, authorities
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    log.error("ERROR ", e);
                }
            } else {
                log.error("Invalid token.");
            }
        }
        if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
        }
    }

}
