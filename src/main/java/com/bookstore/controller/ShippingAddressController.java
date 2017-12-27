package com.bookstore.controller;

import com.bookstore.domain.Shipping;
import com.bookstore.domain.User;
import com.bookstore.domain.enums.UsaStatesConstants;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by User on 15.12.2017.
 */
@Controller
@RequestMapping("/shippingAddress")
public class ShippingAddressController {

    private final UserService userService;

    @Autowired
    public ShippingAddressController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("add")
    public String add(Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("shipping", new Shipping());

//        List<String> stateList = USConstants.listOfUSStatesCode;
//        Collections.sort(stateList);
//        model.addAttribute("stateList", USConstants.listOfUSStatesCode);
        model.addAttribute("stateList", UsaStatesConstants.values());
        model.addAttribute("userPaymentList", user.getPaymentList());
        model.addAttribute("userShippingList", user.getShippingList());
//            model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @GetMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getPaymentList());
        model.addAttribute("userShippingList", user.getShippingList());
//        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("listOfCreditCards", true);
        return "myProfile";
    }
}
