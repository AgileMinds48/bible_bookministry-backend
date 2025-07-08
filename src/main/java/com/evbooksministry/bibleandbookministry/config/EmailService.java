package com.evbooksministry.bibleandbookministry.config;

import com.evbooksministry.bibleandbookministry.dtos.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

//    static Dotenv dotenv = Dotenv.configure().load();

    private static final String appEmail = "elikemfenuksu@gmail.com";

    public EmailService(JavaMailSender mailSender,
                        TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }


    @Async
    public void sendEmail(EmailRequest request, String template, Context context) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String htmlContent = templateEngine.process(template, context);

            helper.setFrom(appEmail);
            helper.setTo(request.recipient());
            helper.setSubject(request.subject());
            helper.setText(htmlContent, true);

            System.out.println("email: " + htmlContent);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
