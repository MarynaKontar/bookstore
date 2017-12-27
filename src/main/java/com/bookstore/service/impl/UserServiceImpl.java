package com.bookstore.service.impl;

import com.bookstore.domain.Billing;
import com.bookstore.domain.Payment;
import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.PaymentRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by User on 16.11.2017.
 */
@Service
public class UserServiceImpl implements UserService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PaymentRepository paymentRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, RoleRepository roleRepository, PaymentRepository paymentRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken;
//        if(userRepository.exists(user.getId())){
//
//            myToken.updateToken(token);
//        }
        myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {
            LOGGER.info("User with username {} already exists. Nothing will be done", user.getUsername());
//            throw new Exception("User with username " + user.getUsername() + "already exists. Nothing will be done");
        } else {
            userRoles.forEach(userRole -> roleRepository.save(userRole.getRole()));
            user.getUserRoles().addAll(userRoles);
            localUser = userRepository.save(user);
        }
        return localUser;
    }

    @Override
    @Transactional
    public User save(User user) {
       return userRepository.save(user);
    }

    @Override
    @Transactional
    public void addOrUpdatePayment(User user, Payment payment, Billing billing) {

        //for update credit card (payment)
        if(billing.getId() != null){payment.removeBilling();}
        if(payment.getId() != null){
            Payment payment1 = paymentRepository.findOne(payment.getId());
            //if updated credit card is default:
            if(payment1.isDefaultPayment()){payment.setDefaultPayment(true);}
            user.removePayment(payment1);
        } else {//for new credit card
            //make all user payments non default except new payment
            user.getPaymentList().forEach(payment1 -> payment1.setDefaultPayment(false));
            payment.setDefaultPayment(true);}

        payment.addBilling(billing);
        user.addPayment(payment);
        userRepository.save(user);
    }
}


