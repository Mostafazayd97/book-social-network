package com.mostafa.book.network.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    @Async
    public void sendEmail (String to,
                           String subject,
                           String userName,
                           String ConfirmationUrl,
                           String ActivationCode,
                           EmailTemplateName emailTemplateName) throws MessagingException {

        String templateName ;
        if(emailTemplateName == null){
            templateName = "confirm_template";
        }else{
            templateName = emailTemplateName.getName();
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String,Object> properties = new HashMap<>();
        properties.put("username",userName);
        properties.put("confirmationUrl",ConfirmationUrl);
        properties.put("activation_code",ActivationCode);
        Context context = new Context();
        context.setVariables(properties);
        helper.setFrom("contact@mostafazayed.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String template = templateEngine.process(templateName,context);
        helper.setText(template,true);

        mailSender.send(message);

    }
}
