package com.farmflow.service.email.impl;

import com.farmflow.dto.EmailRequest;
import com.farmflow.exception.EmailServiceDisabledException;
import com.farmflow.service.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final AtomicBoolean emailServiceEnabled  = new AtomicBoolean( true);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
    @Override
    public void sendEmail(EmailRequest emailRequest) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailRequest.getRecipientEmail());
        helper.setSubject(emailRequest.getSubject());
        String content = emailRequest.isHtml()
                ? processHtmlTemplate(emailRequest.getTemplateName(), emailRequest.getContextVariables())
                : emailRequest.getBody();
        helper.setText(content, emailRequest.isHtml());

        mailSender.send(message);
    }

    @Override
    public void sendEmailAndAttachment(EmailRequest emailRequest, MultipartFile[] fileList) throws IOException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailRequest.getRecipientEmail());
        helper.setSubject(emailRequest.getSubject());
        String content = emailRequest.isHtml()
                ? processHtmlTemplate(emailRequest.getTemplateName(), emailRequest.getContextVariables())
                : emailRequest.getBody();
        helper.setText(content, emailRequest.isHtml());

        // Add attachments
        if (fileList != null) {
            for (MultipartFile file : fileList) {
                if (!file.isEmpty()) {
                    ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes());
                    helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), byteArrayResource);
                }
            }
        }

        mailSender.send(message);
    }
    @Override
    public boolean isEmailServiceEnabled() {
        return !emailServiceEnabled.get();
    }

    @Override
    public void toggleEmailService() {
        emailServiceEnabled.set(!emailServiceEnabled.get());
        log.info("Email service toggled to: {}", emailServiceEnabled.get() ? "Enabled" : "Disabled");
    }

    private String processHtmlTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        if (variables != null) {
            variables.forEach(context::setVariable);
        }
        try {
            return templateEngine.process(templateName, context);
        } catch (TemplateInputException e) {
            log.error("Failed to process template {}: {}", templateName, e.getMessage());
            throw e;
        }
    }

}