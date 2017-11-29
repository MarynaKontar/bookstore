package com.bookstore.repository;

import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by User on 16.11.2017.
 */
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);
    List<PasswordResetToken> findAllByExpiryDateLessThan(LocalDateTime now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllByExpiryDateBefore(LocalDateTime now);
//    void deleteAllByExpiryDateBefore(LocalDateTime now);
//    void deleteByExpiryDateLessThan(LocalDateTime now);
}
