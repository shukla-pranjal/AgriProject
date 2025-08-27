package com.farmflow.service.email;

import com.farmflow.dto.FarmerDTO;
import com.farmflow.dto.PaymentDTO;
import com.farmflow.dto.UserDTO;
import com.farmflow.entity.*;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.enums.PaymentMethod;

public interface EmailComposerService {
    String sendVerificationEmail(UserDTO userDTO);

    void sendLowStockAlert(Product product, Farmer farmer);

    void sendFarmerWelcomeEmail(FarmerDTO farmerDTO, User user);

    void sendOrderConfirmation(Orders saved, User user);

    void sendOrderCancellation(Orders orders, User user);

    void sendStatusUpdate(Orders orders, User user, OrdersStatus status);

    void sendPaymentReceipt(PaymentDTO paymentDTO, User user);

    void sendPaymentMethodUpdated(Payment payment, PaymentMethod method);

    void sendPaymentUpdateNotification(Payment updatedPayment, PaymentDTO paymentDTO);

    void newProductCreated(Farmer farmer, Product product);

    void productUpdated(Farmer farmer, Product updatedProduct);

    void sendAccountDeletionNotification(User existingUser);

    void sendRolePromoted(User user);

    void sendRoleDemoted(User user);
}
