package com.nearloo.nearloo_backend.service;

import com.nearloo.nearloo_backend.dto.*;
import com.nearloo.nearloo_backend.entity.User;
import com.nearloo.nearloo_backend.repository.UserRepository;
import com.nearloo.nearloo_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Map<String, String> register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already exists");

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return Map.of("token", token, "role", user.getRole(), "name", user.getName());
    }

    public Map<String, String> login(LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Wrong password");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return Map.of("token", token, "role", user.getRole(), "name", user.getName());
    }
}
