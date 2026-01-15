package com.farmflow.service.email.impl;

import com.farmflow.dto.*;
import com.farmflow.entity.*;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.Role;
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailComposerServiceImpl implements EmailComposerService {
    private final EmailService emailService;


    @Override
    public String sendVerificationEmail(UserDTO userDTO) {
        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Verification email not sent to user: {}", userDTO.getEmail());
            return null;
        }
        final String VERIFICATION_SUBJECT = "Verify Your Email Address";
        final String TEMPLATE_NAME = "verification-email";

        String verificationCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("name", userDTO.getName());
        contextVariables.put("verificationCode", verificationCode);

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(VERIFICATION_SUBJECT)
                    .subject(VERIFICATION_SUBJECT)
                    .recipientEmail(userDTO.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME) // Specify template
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Verification email sent successfully to {}", userDTO.getEmail());
            return verificationCode;
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", userDTO.getEmail(), e.getMessage());
            return null;
        }
    }

    @Override
    public void sendLowStockAlert(Product product, Farmer farmer) {
        final String LOW_STOCK_SUBJECT = "Low Stock Alert for " + product.getName();
        final String TEMPLATE_NAME = "low-stock-alert";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Low stock alert not sent for product: {} to farmer: {}",
                    product.getName(), farmer.getUser().getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("farmerName", farmer.getUser().getName());
        contextVariables.put("productName", product.getName());
        contextVariables.put("quantity", product.getQuantity());
        contextVariables.put("unit", product.getUnit().toString());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(LOW_STOCK_SUBJECT)
                    .subject(LOW_STOCK_SUBJECT)
                    .recipientEmail(farmer.getUser().getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Low stock alert sent successfully to {} for product {}",
                    farmer.getUser().getEmail(), product.getName());
        } catch (Exception e) {
            log.error("Failed to send low stock alert to {} for product {}: {}",
                    farmer.getUser().getEmail(), product.getName(), e.getMessage());
        }
    }

    @Override
    public void sendFarmerWelcomeEmail(FarmerDTO farmerDTO, User user) {
        final String WELCOME_SUBJECT = "Welcome to Our Farming Community!";
        final String TEMPLATE_NAME = "farmer-welcome";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Welcome email not sent to farmer: {}", user.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("farmerName", user.getName());
        contextVariables.put("farmName", farmerDTO.getFarmName());
        contextVariables.put("farmType", farmerDTO.getFarmType());
        contextVariables.put("supportEmail", "support@farmconnect.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(WELCOME_SUBJECT)
                    .subject(WELCOME_SUBJECT)
                    .recipientEmail(user.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Farmer welcome email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    @Override
    public void sendOrderConfirmation(Orders order, User user) {
        final String CONFIRMATION_SUBJECT = "Order Confirmation #" + order.getId();
        final String TEMPLATE_NAME = "order-confirmation";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Order confirmation not sent for order: {} to user: {}",
                    order.getId(), user.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("order", order);
        contextVariables.put("user", user);
        contextVariables.put("orderDate", formatOrderDate(order.getOrderDate()));
        contextVariables.put("orderItems", order.getItems());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(CONFIRMATION_SUBJECT)
                    .subject(CONFIRMATION_SUBJECT)
                    .recipientEmail(user.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Order confirmation sent successfully for order {} to {}",
                    order.getId(), user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send order confirmation for order {}: {}",
                    order.getId(), e.getMessage());
        }
    }

    private String formatOrderDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        return dateTime.format(formatter);
    }

    @Override
    public void sendOrderCancellation(Orders orders, User user) {
        final String CANCELLATION_SUBJECT = "Order Cancellation #" + orders.getId();
        final String TEMPLATE_NAME = "order-cancellation";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Order cancellation not sent for order: {} to user: {}",
                    orders.getId(), user.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", user.getName());
        contextVariables.put("orderId", orders.getId());
        contextVariables.put("orderDate", formatOrderDate(orders.getOrderDate()));
        contextVariables.put("totalPrice", orders.getTotalPrice());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(CANCELLATION_SUBJECT)
                    .subject(CANCELLATION_SUBJECT)
                    .recipientEmail(user.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Order cancellation email sent successfully for order {} to {}",
                    orders.getId(), user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send order cancellation email for order {} to {}: {}",
                    orders.getId(), user.getEmail(), e.getMessage());
        }
    }

    @Override
    public void sendStatusUpdate(Orders orders, User user, OrdersStatus status) {
        final String STATUS_SUBJECT = "Order Status Update #" + orders.getId();
        final String TEMPLATE_NAME = "order-status-update";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Status update not sent for order: {} to user: {}",
                    orders.getId(), user.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", user.getName());
        contextVariables.put("orderId", orders.getId());
        contextVariables.put("orderDate", formatOrderDate(orders.getOrderDate()));
        contextVariables.put("totalPrice", orders.getTotalPrice());
        contextVariables.put("status", status.name());
        contextVariables.put("statusDescription", status.toString());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(STATUS_SUBJECT)
                    .subject(STATUS_SUBJECT)
                    .recipientEmail(user.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Order status update email sent successfully for order {} to {} with status {}",
                    orders.getId(), user.getEmail(), status.name());
        } catch (Exception e) {
            log.error("Failed to send order status update email for order {} to {}: {}",
                    orders.getId(), user.getEmail(), e.getMessage());
        }
    }
    @Override
    public void sendPaymentReceipt(PaymentDTO paymentDTO, User user) {
        final String RECEIPT_SUBJECT = "Payment Receipt for Transaction #" + paymentDTO.getTransactionId();
        final String TEMPLATE_NAME = "payment-receipt";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Payment receipt not sent for transaction: {} to user: {}",
                    paymentDTO.getTransactionId(), user.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", user.getName());
        contextVariables.put("transactionId", paymentDTO.getTransactionId());
        contextVariables.put("amount", paymentDTO.getAmount());
        contextVariables.put("ordersId", paymentDTO.getOrdersId());
        contextVariables.put("paymentStatus", paymentDTO.getPaymentStatus().name());
        contextVariables.put("paymentMethod", paymentDTO.getPaymentMethod().name());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(RECEIPT_SUBJECT)
                    .subject(RECEIPT_SUBJECT)
                    .recipientEmail(user.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Payment receipt email sent successfully for transaction {} to {}",
                    paymentDTO.getTransactionId(), user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send payment receipt email for transaction {} to {}: {}",
                    paymentDTO.getTransactionId(), user.getEmail(), e.getMessage());
        }
    }

    @Override
    public void sendPaymentMethodUpdated(Payment payment, PaymentMethod method) {
        final String UPDATE_SUBJECT = "Payment Method Updated for Transaction #" + payment.getTransactionId();
        final String TEMPLATE_NAME = "payment-method-updated";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Payment method update not sent for transaction: {} to user: {}",
                    payment.getTransactionId(), payment.getOrders().getUser().getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", payment.getOrders().getUser().getName());
        contextVariables.put("transactionId", payment.getTransactionId());
        contextVariables.put("amount", payment.getAmount());
        contextVariables.put("ordersId", payment.getOrders().getId());
        contextVariables.put("paymentMethod", method.name());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(UPDATE_SUBJECT)
                    .subject(UPDATE_SUBJECT)
                    .recipientEmail(payment.getOrders().getUser().getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Payment method update email sent successfully for transaction {} to {}",
                    payment.getTransactionId(), payment.getOrders().getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send payment method update email for transaction {} to {}: {}",
                    payment.getTransactionId(), payment.getOrders().getUser().getEmail(), e.getMessage());
        }
    }

    @Override
    public void sendPaymentUpdateNotification(Payment updatedPayment, PaymentDTO paymentDTO) {
        final String UPDATE_SUBJECT = "Payment Update Notification for Transaction #" + updatedPayment.getTransactionId();
        final String TEMPLATE_NAME = "payment-update-notification";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Payment update notification not sent for transaction: {} to user: {}",
                    updatedPayment.getTransactionId(), updatedPayment.getOrders().getUser().getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", updatedPayment.getOrders().getUser().getName());
        contextVariables.put("transactionId", updatedPayment.getTransactionId());
        contextVariables.put("amount", updatedPayment.getAmount());
        contextVariables.put("ordersId", updatedPayment.getOrders().getId());
        contextVariables.put("paymentStatus", updatedPayment.getPaymentStatus().name());
        contextVariables.put("paymentMethod", updatedPayment.getPaymentMethod().name());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(UPDATE_SUBJECT)
                    .subject(UPDATE_SUBJECT)
                    .recipientEmail(updatedPayment.getOrders().getUser().getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Payment update notification email sent successfully for transaction {} to {}",
                    updatedPayment.getTransactionId(), updatedPayment.getOrders().getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send payment update notification email for transaction {} to {}: {}",
                    updatedPayment.getTransactionId(), updatedPayment.getOrders().getUser().getEmail(), e.getMessage());
        }
    }

    @Override
    public void newProductCreated(Farmer farmer, Product product) {
        final String NEW_PRODUCT_SUBJECT = "New Product Created: " + product.getName();
        final String TEMPLATE_NAME = "new-product-created";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. New product notification not sent for product: {} to farmer: {}",
                    product.getName(), farmer.getUser().getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("farmerName", farmer.getUser().getName());
        contextVariables.put("productName", product.getName());
        contextVariables.put("quantity", product.getQuantity());
        contextVariables.put("unit", product.getUnit().toString());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(NEW_PRODUCT_SUBJECT)
                    .subject(NEW_PRODUCT_SUBJECT)
                    .recipientEmail(farmer.getUser().getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("New product notification email sent successfully for product {} to {}",
                    product.getName(), farmer.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send new product notification email for product {} to {}: {}",
                    product.getName(), farmer.getUser().getEmail(), e.getMessage());
        }
    }

    @Override
    public void productUpdated(Farmer farmer, Product updatedProduct) {
        final String UPDATE_SUBJECT = "Product Updated: " + updatedProduct.getName();
        final String TEMPLATE_NAME = "product-updated";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Product update notification not sent for product: {} to farmer: {}",
                    updatedProduct.getName(), farmer.getUser().getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("farmerName", farmer.getUser().getName());
        contextVariables.put("productName", updatedProduct.getName());
        contextVariables.put("quantity", updatedProduct.getQuantity());
        contextVariables.put("unit", updatedProduct.getUnit().toString());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(UPDATE_SUBJECT)
                    .subject(UPDATE_SUBJECT)
                    .recipientEmail(farmer.getUser().getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Product update notification email sent successfully for product {} to {}",
                    updatedProduct.getName(), farmer.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send product update notification email for product {} to {}: {}",
                    updatedProduct.getName(), farmer.getUser().getEmail(), e.getMessage());
        }
    }

    @Override
    public void sendAccountDeletionNotification(User existingUser) {
        final String DELETION_SUBJECT = "Account Deletion Notification";
        final String TEMPLATE_NAME = "account-deletion-notification";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Account deletion notification not sent to user: {}",
                    existingUser.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", existingUser.getName());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(DELETION_SUBJECT)
                    .subject(DELETION_SUBJECT)
                    .recipientEmail(existingUser.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Account deletion notification email sent successfully to {}",
                    existingUser.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account deletion notification email to {}: {}",
                    existingUser.getEmail(), e.getMessage());
        }
    }

    @Override
    public String sendPasswordResetEmail(UserDTO userDTO) {
        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Password reset email not sent to user: {}", userDTO.getEmail());
            return null;
        }

        final String RESET_SUBJECT = "Password Reset Request";
        final String TEMPLATE_NAME = "password-reset-email";

        String resetCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("name", userDTO.getName());
        contextVariables.put("resetCode", resetCode);
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(RESET_SUBJECT)
                    .subject(RESET_SUBJECT)
                    .recipientEmail(userDTO.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Password reset email sent successfully to {}", userDTO.getEmail());
            return resetCode;
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", userDTO.getEmail(), e.getMessage());
            return null;
        }
    }

    @Override
    public void sendEmailChangeAlert(ChangeEmailRequest request) {
        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Email change alert not sent for user: {}", request.getCurrentPassword());
            return;
        }

        final String CHANGE_ALERT_SUBJECT = "Email Change Notification";
        final String TEMPLATE_NAME = "email-change-alert";

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("oldEmail", request.getCurrentPassword());
        contextVariables.put("newEmail", request.getNewEmail());
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(CHANGE_ALERT_SUBJECT)
                    .subject(CHANGE_ALERT_SUBJECT)
                    .recipientEmail(request.getCurrentPassword()) // send to old email for security reasons
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Email change alert sent successfully to old email {} about change to {}",
                    request.getCurrentPassword(), request.getNewEmail());
        } catch (Exception e) {
            log.error("Failed to send email change alert to {}: {}", request.getCurrentPassword(), e.getMessage());
        }
    }

    @Override
    public void sendRolePromoted(User user) {
        final String PROMOTION_SUBJECT = "Your Account Role Has Been Promoted";
        final String TEMPLATE_NAME = "role-promoted";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Role promotion notification not sent to user: {}",
                    user.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", user.getName());
        contextVariables.put("roles", user.getRoles().stream().map(Role::name).collect(Collectors.joining(", ")));
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(PROMOTION_SUBJECT)
                    .subject(PROMOTION_SUBJECT)
                    .recipientEmail(user.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Role promotion notification email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send role promotion notification email to {}: {}",
                    user.getEmail(), e.getMessage());
        }
    }
    @Override
    public void sendRoleDemoted(User user) {
        final String DEMOTION_SUBJECT = "Your Account Role Has Been Updated";
        final String TEMPLATE_NAME = "role-demoted";

        if (!emailService.isEmailServiceEnabled()) {
            log.warn("Email service is disabled. Role demotion notification not sent to user: {}",
                    user.getEmail());
            return;
        }

        // Prepare context variables
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", user.getName());
        contextVariables.put("roles", user.getRoles().stream().map(Role::name).collect(Collectors.joining(", ")));
        contextVariables.put("supportEmail", "support@yourdomain.com");
        contextVariables.put("currentYear", LocalDate.now().getYear());

        try {
            // Build email request
            EmailRequest emailRequest = EmailRequest.builder()
                    .title(DEMOTION_SUBJECT)
                    .subject(DEMOTION_SUBJECT)
                    .recipientEmail(user.getEmail())
                    .isHtml(true)
                    .templateName(TEMPLATE_NAME)
                    .contextVariables(contextVariables)
                    .build();

            // Send email
            emailService.sendEmail(emailRequest);
            log.info("Role demotion notification email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send role demotion notification email to {}: {}",
                    user.getEmail(), e.getMessage());
        }
    }
}
