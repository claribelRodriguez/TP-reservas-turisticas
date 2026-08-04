package com.app.TPreservasturisticas.service;

import com.app.TPreservasturisticas.config.JwtService;
import com.app.TPreservasturisticas.dto.LoginDTO;
import com.app.TPreservasturisticas.entity.Usuario;
import com.app.TPreservasturisticas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginDTO loginDto) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(loginDto.nombreUsuario())
                .orElseThrow(() -> new RuntimeException("usuario o contraseña invalidos"));


        if (!passwordEncoder.matches(loginDto.contrasenia(), usuario.getContrasenia())) {
            throw new RuntimeException("usuario o contraseña invalidos");
        }

        return jwtService.generarToken(usuario.getNombreUsuario(), usuario.getRol().name());
    }
}
