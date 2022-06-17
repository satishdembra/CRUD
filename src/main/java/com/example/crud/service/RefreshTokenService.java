package com.example.crud.service;

import com.example.crud.Exception.CRUDException;
import com.example.crud.model.RefreshToken;
import com.example.crud.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Transactional
@AllArgsConstructor
@Service
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }
    public void validateToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new CRUDException("Invalid Refresh Token"));
    }
    public void deleteToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
