package com.bookstore.service.impl;

import com.bookstore.domain.Payment;
import com.bookstore.repository.PaymentRepository;
import com.bookstore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by User on 21.12.2017.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findOne(id);
    }
}
