package com.farmflow.service.email;

import com.farmflow.dto.EmailRequest;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmailService {
    void sendEmail(EmailRequest emailRequest) throws Exception;
    void sendEmailAndAttachment(EmailRequest emailRequest, MultipartFile[] fileList)throws IOException, MessagingException;
    boolean isEmailServiceEnabled();
    void toggleEmailService();

}
