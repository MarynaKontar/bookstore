package com.bookstore.repository;

import com.bookstore.domain.Payment;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by User on 21.12.2017.
 */
public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
