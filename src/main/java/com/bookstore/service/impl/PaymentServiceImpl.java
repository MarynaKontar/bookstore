package com.bookstore.service.impl;

import com.bookstore.domain.Billing;
import com.bookstore.domain.Payment;
import com.bookstore.domain.User;
import com.bookstore.repository.PaymentRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * Created by User on 21.12.2017.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Payment findById(Long id) {
        return paymentRepository.findOne(id);
    }

    @Override
    @Transactional
    public void addOrUpdatePayment(User user, Payment payment, Billing billing) {

        //for update credit card (payment)
        if (billing.getId() != null) {
            payment.removeBilling();
        }
        if (payment.getId() != null) {
            Payment payment1 = paymentRepository.findOne(payment.getId());
            //if updated credit card is default:
            if (payment1.isDefaultPayment()) {
                payment.setDefaultPayment(true);
            }
            user.removePayment(payment1);
        } else {//for new credit card
            //make all user payments non default except new payment
            user.getPaymentList().forEach(payment1 -> payment1.setDefaultPayment(false));
            payment.setDefaultPayment(true);
        }

        payment.addBilling(billing);
        user.addPayment(payment);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deletePaymentById(Long id) {
        Payment payment = paymentRepository.findOne(id);
        if (payment != null) {
            User user = userRepository.findOne(payment.getUser().getId());

            //if deleted payment is default
            if (payment.isDefaultPayment()) {
                user.getPaymentList()
                        .stream()
                        .findFirst()
                        .ifPresent(payment1 -> payment1.setDefaultPayment(true));
            }
            user.removePayment(payment);
        }

    }

    @Override
    @Transactional
    public void setDefaultPayment(User user, Long defaultPaymentId) {
//        user.getPaymentList()
//                .stream()
//                .filter(payment -> payment.getId()==defaultPaymentId)
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("There is no payment (credit card) with id = " + defaultPaymentId));

        if(user.getPaymentList()
                .stream()
                .noneMatch(payment -> payment.getId()==defaultPaymentId)){
            throw new RuntimeException("There is no shipping with id = " + defaultPaymentId);}

        user.getPaymentList()
                .forEach(payment -> payment.setDefaultPayment(payment.getId() == defaultPaymentId));

    }

}
