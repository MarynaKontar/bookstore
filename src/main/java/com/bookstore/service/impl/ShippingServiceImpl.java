package com.bookstore.service.impl;

import com.bookstore.domain.Shipping;
import com.bookstore.domain.User;
import com.bookstore.repository.ShippingRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShippingServiceImpl(ShippingRepository shippingRepository, UserRepository userRepository) {
        this.shippingRepository = shippingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Shipping findById(Long id) {
        return shippingRepository.findOne(id);
    }

    @Override
    @Transactional
    public void addOrUpdateShipping(User user, Shipping shipping) {

        //for update shipping address
        if (shipping.getId() != null) {
            Shipping shipping1 = shippingRepository.findOne(shipping.getId());
            //if updated shipping is default:
            if (shipping1.isDefaultShipping()) {
                shipping.setDefaultShipping(true);
            }
            user.removeShipping(shipping1);
        } else {//for new shipping address
            //make all user shippings non default except new shipping
            user.getShippingList().forEach(shipping1 -> shipping1.setDefaultShipping(false));
            shipping.setDefaultShipping(true);
        }

        user.addShipping(shipping);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteShippingById(Long id) {
        Shipping shipping = shippingRepository.findOne(id);
        if (shipping != null) {
            User user = userRepository.findOne(shipping.getUser().getId());

            //if deleted shipping is default
            if (shipping.isDefaultShipping()) {
                user.getShippingList()
                        .stream()
                        .findFirst()
                        .ifPresent(shipping1 -> shipping1.setDefaultShipping(true));
            }

            user.removeShipping(shipping);
        }
    }

    @Override
    @Transactional
    public void setDefaultShipping(User user, Long defaultShippingId) {
//                user.getShippingList()
//                        .stream()
//                        .filter(shipping -> shipping.getId()==defaultShippingId)
//                        .findFirst()
//                        .orElseThrow(() -> new RuntimeException("There is no shipping with id = " + defaultShippingId));

        if(user.getShippingList()
                .stream()
                .noneMatch(shipping -> shipping.getId()==defaultShippingId)){
            throw new RuntimeException("There is no shipping with id = " + defaultShippingId);}

        user.getShippingList()
                .forEach(shipping -> shipping.setDefaultShipping(shipping.getId() == defaultShippingId));
    }


}
