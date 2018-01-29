package com.bookstore.controller;

import com.bookstore.domain.Payment;
import com.bookstore.domain.Shipping;
import com.bookstore.domain.User;
import com.bookstore.domain.enums.UsaStatesConstants;
import com.bookstore.service.ShippingService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 15.12.2017.
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {

    private final UserService userService;
    private final ShippingService shippingService;

    @Autowired
    public ShippingController(UserService userService, ShippingService shippingService) {
        this.userService = userService;
        this.shippingService = shippingService;
    }

    @GetMapping("/add")
    public String add(Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewShipping", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("shipping", new Shipping());

//        List<String> stateList = USConstants.listOfUSStatesCode;
//        Collections.sort(stateList);
//        model.addAttribute("stateList", USConstants.listOfUSStatesCode);
        model.addAttribute("stateList", UsaStatesConstants.values());
        model.addAttribute("userPaymentList", user.getPaymentList());
        model.addAttribute("userShippingList", user.getShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("shipping") Shipping shipping,
                      Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        shippingService.addOrUpdateShipping(user, shipping);

        List<Shipping> shippings = user.getShippingList();
       shippings.sort(Comparator.comparing(Shipping::getName));

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getPaymentList());
        model.addAttribute("userShippingList", shippings);
        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        return "myProfile";
    }




    @GetMapping("/shippings")
    public String shippings(Model model, Principal principal, HttpServletRequest request) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getPaymentList());
        model.addAttribute("userShippingList", user.getShippingList());
        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("listOfCreditCards", true);
        return "myProfile";
    }

    @GetMapping("/update")
    public String update(@ModelAttribute("id") Long shippingId, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Shipping shipping = shippingService.findById(shippingId);

        if (user.getId() != shipping.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("shipping", shipping);
            model.addAttribute("user", user);

            model.addAttribute("classActiveShipping", true);
            //TODO показывает и список и апдейт
//            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("addNewShipping", true);

            model.addAttribute("userPaymentList", user.getPaymentList());
            model.addAttribute("userShippingList", user.getShippingList());
            model.addAttribute("orderList", user.getOrderList());

            model.addAttribute("stateList", UsaStatesConstants.values());

            return "myProfile";
        }
    }

    @GetMapping("/remove")
    public String remove(@ModelAttribute("id") Long shippingId, Principal principal, Model model
    ){
        User user = userService.findByUsername(principal.getName());
        Shipping shipping = shippingService.findById(shippingId);

        if(user.getId() != shipping.getUser().getId()) {
            return "badRequestPage";
        } else {
            shippingService.deleteShippingById(shippingId);
            model.addAttribute("user", user);
//            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getPaymentList());
            model.addAttribute("userShippingList", user.getShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @PostMapping("/setDefault")
    public String setDefault(@ModelAttribute("defaultShippingId") Long defaultShippingId,
                             Principal principal, Model model){

        User user = userService.findByUsername(principal.getName());
        shippingService.setDefaultShipping(user, defaultShippingId);
        model.addAttribute("user", user);
//        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getPaymentList());
        model.addAttribute("userShippingList", user.getShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }




}
