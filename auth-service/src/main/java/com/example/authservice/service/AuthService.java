package com.example.authservice.service;

import com.example.authservice.dto.*;
import java.util.List;
import java.util.UUID;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
    UserDto me(String email);
    UserDto getUserById(UUID id);
    List<UserDto> getAllUsers();
}
