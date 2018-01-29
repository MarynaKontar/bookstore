package com.bookstore.service;

import com.bookstore.domain.Shipping;
import com.bookstore.domain.User;

public interface ShippingService {
    Shipping findById(Long id);

    void addOrUpdateShipping(User user, Shipping shipping);

    void deleteShippingById(Long id);

    void setDefaultShipping(User user, Long defaultShippingId);
}
