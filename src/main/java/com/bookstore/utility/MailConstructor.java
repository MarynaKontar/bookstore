package com.bookstore.utility;

import com.bookstore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by User on 22.11.2017.
 */
@Component
public class MailConstructor {

    private final Environment environment;

    @Autowired
    public MailConstructor(Environment environment) {
        this.environment = environment;
    }

    public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token,
                                                      User user, String password) {
        String url = contextPath + "/newUser?token=" + token;
        String message = "\n ЗАЯ, ПРОСТИ, ЭТО Я ТРЕНЕРУЮСЬ ОТПРАВЛЯТЬ EMAIL ЧЕРЕЗ СВОЕ ПРИЛОЖЕНИЕ. " +
                "\nНЕ ТРОГАЙ, Я ПОТОМ ВСЕ УДАЛЮ" +
                "\nPlease click on this link to verify your email and edit your personal information." +
                "Your password is: \n" + password;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Le's Bookstore - New User");
        email.setText(url + message);
        email.setFrom(environment.getProperty("support.email"));
        return email;
    }
}
