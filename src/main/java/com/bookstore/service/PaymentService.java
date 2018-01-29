package com.bookstore.service;

import com.bookstore.domain.Billing;
import com.bookstore.domain.Payment;
import com.bookstore.domain.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 21.12.2017.
 */
public interface PaymentService {
    Payment findById(Long id);

    void addOrUpdatePayment(User user, Payment payment, Billing billing);

    void deletePaymentById(Long id);

    void setDefaultPayment(User user, Long defaultPaymentId);
}
