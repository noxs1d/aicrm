package com.noxsid.nks.crmai.service;

import com.noxsid.nks.crmai.auth.AuthenticationRequest;
import com.noxsid.nks.crmai.auth.AuthenticationResponse;
import com.noxsid.nks.crmai.auth.RegisterRequest;
import com.noxsid.nks.crmai.data.Position;
import com.noxsid.nks.crmai.data.Role;
import com.noxsid.nks.crmai.data.Token;
import com.noxsid.nks.crmai.data.User;
import com.noxsid.nks.crmai.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public void register(RegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setPosition(Position.JUNIOR);
        user = userRepository.save(user);
    }

    private void revokeAllToken(User user){
        List<Token> tokens = tokenRepository.findAllAccessTokenByUser(user.getId());
        if(!tokens.isEmpty()){
            tokens.forEach(t->t.setLoggedOut(true));
        }
        tokenRepository.saveAll(tokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user){
        Logger logger = LoggerFactory.getLogger("Saving tokens");
        logger.info("saving tokens");
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setUser(user);
        token.setLoggedOut(false);
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        Logger logger = LoggerFactory.getLogger("Authenticate");
        logger.info("Right now at authenticate method");
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllToken(user);
        saveUserToken(accessToken,refreshToken,user);
        return new AuthenticationResponse(accessToken,refreshToken);
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest req,
            HttpServletResponse res
    ){

        String authorHeader = res.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorHeader == null || !authorHeader.startsWith("Bearer ")){
            return null;
        }

        String token = authorHeader.substring(7);
        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Not found user"));
        if(jwtService.isValidRefresh(token, user)){
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllToken(user);
            saveUserToken(accessToken,refreshToken,user);

            return new AuthenticationResponse(accessToken,refreshToken);
        }

        return null;
    }


}
