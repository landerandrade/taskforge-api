package com.forgile.taskforge.service;

import java.util.List;

import com.forgile.taskforge.config.JwtUtil;
import com.forgile.taskforge.dto.AuthResponse;
import com.forgile.taskforge.dto.LoginRequest;
import com.forgile.taskforge.dto.UsuarioRequest;
import com.forgile.taskforge.model.Usuario;
import com.forgile.taskforge.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(List.of())
                .build();
    }

    public AuthResponse registrar(UsuarioRequest request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        repository.save(usuario);

        return new AuthResponse(jwtUtil.generateToken(usuario.getEmail()));
    }

    public AuthResponse autenticar(LoginRequest request) {
        UserDetails userDetails = loadUserByUsername(request.email());

        if (!passwordEncoder.matches(request.senha(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        return new AuthResponse(jwtUtil.generateToken(userDetails.getUsername()));
    }
}
