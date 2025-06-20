package com.noxsid.nks.crmai.service;

import com.noxsid.nks.crmai.data.User;
import com.noxsid.nks.crmai.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final JwtService jwtService;
    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Поиск пользователя в репозитории
        return userRepository.findByUsername(username)
                // Если пользователь не найден, выбрасываем исключение
                .orElseThrow(() ->  new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    public User getUser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String token = null;
        for(Cookie cookie : cookies){
            if("access_token".equals(cookie.getName())){
                token=cookie.getValue();
            }
        }
        if(token==null){
            throw new RuntimeException();
        }
        return userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
    }
}
