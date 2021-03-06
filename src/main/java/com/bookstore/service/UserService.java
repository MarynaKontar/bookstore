package com.bookstore.service;

import com.bookstore.domain.Billing;
import com.bookstore.domain.Payment;
import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;

import java.util.Set;

/**
 * Created by User on 16.11.2017.
 */
public interface UserService {

    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);

    User findByUsername(String username);

    User findByEmail(String email);

    User createUser(User user, Set<UserRole> userRoles) throws Exception;

    User save(User user);

    void addOrUpdatePayment(User user, Payment payment, Billing billing);
}
