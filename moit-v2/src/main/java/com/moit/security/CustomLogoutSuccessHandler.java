package com.moit.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        if (authentication != null &&
            authentication.getPrincipal() instanceof CustomUserDetails user) {

            String provider = user.getProvider();

            if ("kakao".equals(user.getProvider())) {

                String logoutUrl =
                    "https://kauth.kakao.com/oauth/logout"
                    + "?client_id=d1065db6fa6b99aa2d26a3d28c80143a"
                    + "&logout_redirect_uri=http://localhost:8080/user/member/kakaologout";

                response.sendRedirect(logoutUrl);
                return;
            }
        }
        response.sendRedirect("/user/member/login");
    }
}