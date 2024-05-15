package com.onlinebookstore.onlinebookstore.security;

import com.onlinebookstore.onlinebookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    public String authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.email());
        if (user.isEmpty()) {
            throw new RuntimeException("Can't login");
        }
        String userPasswordFromDb = user.get().getPassword();
        String hashedPassword = HashUtil.hashPassword(requestDto.password(), user.get().getSalt());
        if (!hashedPassword.equals(userPasswordFromDb)) {
            throw new RuntimeException("Can't login");
        }
        return tokenUtil.generateToken(requestDto.email());
    }
}
