package com.onlinebookstore.onlinebookstore.security;

import com.onlinebookstore.onlinebookstore.dto.user.UserLoginRequestDto;
import com.onlinebookstore.onlinebookstore.dto.user.UserLoginResponseDto;
import com.onlinebookstore.onlinebookstore.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;


    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                        requestDto.getPassword())
        )
//        Optional<User> user = userRepository.findByEmail(requestDto.email());
//        if (user.isEmpty()) {
//            throw new RuntimeException("Can't login");
//        }
//        String userPasswordFromDb = user.get().getPassword();
//        String hashedPassword = HashUtil.hashPassword(requestDto.password(), user.get().getSalt());
//        if (!hashedPassword.equals(userPasswordFromDb)) {
//            throw new RuntimeException("Can't login");
//        }
        String token = jwtUtil.generateToken(requestDto.getEmail());
        return new UserLoginResponseDto(token);
    }
}
