package com.example.springsecuritysample.jwt;

import com.example.springsecuritysample.model.entity.Users;
import com.example.springsecuritysample.service.UsersService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UsersService usersService;

    public JwtFilter(JwtUtils jwtUtils, UsersService usersService) {
        this.jwtUtils = jwtUtils;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("authorization");
        if (jwt != null) {
            String username = jwtUtils.getUsername(jwt);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Users users = (Users) usersService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(users,null, users.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
