package com.example.crud.service;

import com.example.crud.Exception.CRUDException;
import com.example.crud.dto.LoginDto;
import com.example.crud.dto.RefreshTokenRequest;
import com.example.crud.dto.RegisterDto;
import com.example.crud.dto.AuthDto;
import com.example.crud.model.NotificationEmail;
import com.example.crud.model.RefreshToken;
import com.example.crud.model.User;
import com.example.crud.model.VerificationToken;
import com.example.crud.repository.UserRepository;
import com.example.crud.repository.VerificationTokenRepository;
import com.example.crud.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    @Transactional
    public void signup(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Account Activation CRUDSpring", user.getEmail(), "Your Account Activation URL is http://localhost:8080/api/auth/accountVerification/"+token+" , Please Visit the url to activate your account"));
    }
    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()->new CRUDException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }
    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user= userRepository.findByUsername(username).orElseThrow(() -> new CRUDException("User not Found "+username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthDto login(LoginDto loginDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken();
        return AuthDto.builder()
                .authenticationToken( token)
                .username( loginDto.getUsername())
                .expiry(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .refreshToken(refreshToken.getToken())
                .build();

    }
    public AuthDto refreshToken(RefreshTokenRequest refreshTokenRequest){
         refreshTokenService.validateToken(refreshTokenRequest.getRefreshToken());
         String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthDto.builder()
                .authenticationToken( token)
                .username( refreshTokenRequest.getUsername())
                .expiry(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();

    }
    @Transactional
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }
}
