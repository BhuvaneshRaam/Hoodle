package com.example.hoodle.SSO;

import com.example.hoodle.Entity.Permission;
import com.example.hoodle.Entity.Role;
import com.example.hoodle.Entity.User;
import com.example.hoodle.Repository.UserRepo;
import com.example.hoodle.Service.JwtGenerator;
import jakarta.persistence.Column;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        Optional<User> userOptional = userRepo.findByEmailId(email);

        if (userOptional.isEmpty()) {
            response.sendRedirect(frontendUrl + "/login?error=not_invited");
            return;
        }

        User user = userOptional.get();

        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName())
                .toList();

        List<String> permissions = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Permission perm : role.getPermissions()) {
                permissions.add(perm.getModule().getName() + "." + perm.getPrivilege().getName());
            }
        }

        String token = jwtGenerator.generateJwtToken(user, roleNames, permissions);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true) // IMPORTANT: Change to true when deploying with HTTPS!
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        response.sendRedirect(frontendUrl + "/app");
    }

}
