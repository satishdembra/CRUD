package com.example.crud.controller;

import com.example.crud.dto.LoginDto;
import com.example.crud.dto.RefreshTokenRequest;
import com.example.crud.dto.RegisterDto;
import com.example.crud.dto.AuthDto;
import com.example.crud.service.AuthService;
import com.example.crud.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private RefreshTokenService refreshTokenService;
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterDto registerDto)
    {
        authService.signup(registerDto);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully",HttpStatus.OK);
    }
    @PostMapping("/login")
    public AuthDto login(@RequestBody LoginDto loginDto)
    {
        return authService.login(loginDto);

    }
    @PostMapping("/refresh/token")
    public AuthDto refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
    }
}
