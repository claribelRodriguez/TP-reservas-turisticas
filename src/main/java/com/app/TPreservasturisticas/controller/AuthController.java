package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.dto.LoginDTO;
import com.app.TPreservasturisticas.dto.LoginTokenDTO;
import com.app.TPreservasturisticas.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            String token = authService.login(dto);
            return ResponseEntity.ok(new LoginTokenDTO(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
