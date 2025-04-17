package com.example.nm_auth_service.repository;

import com.example.nm_auth_service.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser_UserId(Integer userId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
