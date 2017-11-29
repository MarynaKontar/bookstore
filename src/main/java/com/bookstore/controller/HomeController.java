package com.bookstore.controller;

import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserService;
import com.bookstore.service.impl.UserSecurityService;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Created by User on 09.11.2017.
 */
@Controller
public class HomeController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;
    private final JavaMailSender mailSender;
    private final MailConstructor mailConstructor;

    @Autowired
    public HomeController(UserService userService, UserSecurityService userSecurityService,
                          JavaMailSender mailSender, MailConstructor mailConstructor) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
        this.mailSender = mailSender;
        this.mailConstructor = mailConstructor;
    }

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("myAccount")
    public String myAccount() {
        return "myAccount";
    }

//    @GetMapping("myProfile")
//    public String myProfile() {
//        return "myProfile";
//    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);
        return "myAccount";
    }

    @PostMapping("/forgetPassword")
    public String forgetPassword(HttpServletRequest request,
                                 @ModelAttribute("email") String email,
                                 Model model
    ) {
        model.addAttribute("classActiveForgetPassword", true);
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        //TODO перенести в сервис и @Transactional
        //set user new password
        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String applUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(applUrl, request.getLocale(),
                token, user, password);

        mailSender.send(newEmail);
        model.addAttribute("forgetPasswordEmailSend", true);

        return "myAccount";
    }

    @PostMapping("/newUser")
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute("email") String userEmail,
            @ModelAttribute("username") String username,
            Model model
    ) throws Exception {
        model.addAttribute("classActiveNewAccount", true);
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);

        if (userService.findByUsername(username) != null) {
            model.addAttribute("usernameExists", true);
            return "myAccount";
        }

        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);
            return "myAccount";
        }

        //TODO перенести в сервис и @Transactional
        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        Role role = new Role();
        role.setRoleId(1);
        role.setName("USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String applUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//
        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(applUrl, request.getLocale(),
                token, user, password);

        mailSender.send(email);
        model.addAttribute("emailSend", true);
        return "myAccount";
    }

    @GetMapping("/newUser")
    public String newUser(Locale locale,
                          @RequestParam("token") String token,
                          Model model) {
        PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

        if (passwordResetToken == null) {
            String message = "Invalid token";
            model.addAttribute("message", message);
            return "redirect:/badRequest";
        }

        User user = passwordResetToken.getUser();

        String username = user.getUsername();

        //set current logged session to user, make sure that current user is logged
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword()
                , userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);
        model.addAttribute("classActiveEdit", true);
        return "myProfile";
    }
}
