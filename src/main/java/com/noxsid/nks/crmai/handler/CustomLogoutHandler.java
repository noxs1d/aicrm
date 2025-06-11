package com.noxsid.nks.crmai.handler;

import com.noxsid.nks.crmai.data.Token;
import com.noxsid.nks.crmai.repository.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler  implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String token = getTokenFromRequest(request);

        if(token==null){
            return;
        }
        Token tokenEntity = tokenRepository.findByAccessToken(token).orElse(null);

        if(tokenEntity != null){
            tokenEntity.setLoggedOut(true);
            tokenRepository.save(tokenEntity);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // Проверяем куки
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Или проверяем заголовок Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
