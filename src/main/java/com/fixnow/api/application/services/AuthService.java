package com.fixnow.api.application.services;

import com.fixnow.api.application.dto.UserDTO;
import com.fixnow.api.application.dto.auth.LoginRequestDTO;
import com.fixnow.api.application.dto.auth.LoginResponseDTO;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import com.fixnow.api.infrastructure.mappers.UserMapper;
import com.fixnow.api.web.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final HttpSecurity http;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginDTO) {

        UserDetails user = userDetailsService.loadUserByUsername(loginDTO.userName());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                loginDTO.password()
        );

        authenticationManager.authenticate(authentication);
        String jwt = jwtService.generateToken(user);

        return new LoginResponseDTO(jwt);
    }

    public UserDTO register(UserDTO userDTO) throws UserAlreadyExistsException {
        if(userRepository.findByUserName(userDTO.userName()).isPresent()) {
            throw new UserAlreadyExistsException(userDTO.userName());
        }

        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return UserMapper.toDTO(userRepository.save(user));
    }

    public void logout() {
        try {
            http.logout(logoutConfig -> {
                logoutConfig.deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
