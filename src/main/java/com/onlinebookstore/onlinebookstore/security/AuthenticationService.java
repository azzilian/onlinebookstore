package com.onlinebookstore.onlinebookstore.security;

import com.onlinebookstore.onlinebookstore.dto.user.UserLoginRequestDto;
import com.onlinebookstore.onlinebookstore.dto.user.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                        requestDto.getPassword())
        );
        String token = jwtUtil.generateToken(requestDto.getEmail());
        return new UserLoginResponseDto(token);
    }
}
