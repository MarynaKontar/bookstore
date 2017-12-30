package com.bookstore.controller;

import com.bookstore.domain.Billing;
import com.bookstore.domain.Payment;
import com.bookstore.domain.User;
import com.bookstore.domain.enums.UsaStatesConstants;
import com.bookstore.service.PaymentService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 15.12.2017.
 */
@Controller
@RequestMapping("/creditCard")
public class CreditCardController {

    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public CreditCardController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @GetMapping("/add")
    public String add(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("billing", new Billing());
        model.addAttribute("payment", new Payment());

//        List<String> stateList = USConstants.listOfUSStatesCode;
//        Collections.sort(stateList);
//        model.addAttribute("stateList", USConstants.listOfUSStatesCode);
        model.addAttribute("stateList",UsaStatesConstants.values());
        model.addAttribute("userPaymentList", user.getPaymentList());
        model.addAttribute("userShippingList", user.getShippingList());
//            model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userPayment") Payment payment,
                      @ModelAttribute("billing") Billing billing, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        userService.addOrUpdatePayment(user, payment, billing);

        List<Payment> payments = user.getPaymentList();
        payments.sort(Comparator.comparing(Payment::getCardName));

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", payments);
        model.addAttribute("userShippingList", user.getShippingList());
//        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        return "myProfile";
    }

    @GetMapping("/creditCards")
    public String listOfCreditCards(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        List<Payment> payments = user.getPaymentList();
        payments.sort(Comparator.comparing(Payment::getCardName));

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", payments);
        model.addAttribute("userShippingList", user.getShippingList());
//        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("listOfCreditCards", true);

        return "myProfile";
    }

    @GetMapping("/update")
    public String update(@ModelAttribute("id") Long creditCardId, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Payment payment = paymentService.findById(creditCardId);

        if (user.getId() != payment.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("billing", payment.getBilling());
            model.addAttribute("payment", payment);

            model.addAttribute("user", user);
            model.addAttribute("userPaymentList", user.getPaymentList());
            model.addAttribute("userShippingList", user.getShippingList());
//            model.addAttribute("orderList", user.getOrderList());
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("addNewCreditCard", true);

            //        List<String> stateList = USConstants.listOfUSStatesCode;
//        Collections.sort(stateList);
//            model.addAttribute("stateList", USConstants.listOfUSStatesCode);
            model.addAttribute("stateList", UsaStatesConstants.values());

            return "myProfile";
        }
    }

    @GetMapping("/remove")
    public String remove(@ModelAttribute("id") Long creditCardId, Principal principal, Model model
    ){
        User user = userService.findByUsername(principal.getName());
        Payment userPayment = paymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()) {
            return "badRequestPage";
        } else {
            userService.deletePaymentById(creditCardId);
            model.addAttribute("user", user);
            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getPaymentList());
            model.addAttribute("userShippingList", user.getShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

}
