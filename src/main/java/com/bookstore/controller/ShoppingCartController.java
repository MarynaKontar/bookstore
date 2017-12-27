package com.bookstore.controller;

import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by User on 15.12.2017.
 */
@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private final UserService userService;

    @Autowired
    public ShoppingCartController(UserService userService) {
        this.userService = userService;
    }
}
