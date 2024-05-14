package com.onlinebookstore.onlinebookstore.security;

import com.onlinebookstore.onlinebookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String gmail)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(gmail)
                .orElseThrow(() -> new UsernameNotFoundException("Can`t find user by email"));
    }
}
