package com.bookstore.service;

import com.bookstore.domain.Payment;

/**
 * Created by User on 21.12.2017.
 */
public interface PaymentService {
    Payment findById(Long id);
}
