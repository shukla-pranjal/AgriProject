package com.farmflow.service;

import com.farmflow.dto.*;
import com.farmflow.exception.ResourceNotFoundException;

public interface AuthService  {
    LoginResponse login(LoginRequest loginRequest) throws ResourceNotFoundException;
    UserDTO createUser(UserDTO userDTO);
    boolean isOwnerOrAdmin(Integer resourceOwnerId);
    void validateUser(Integer id);

    void verifyEmail(EmailVerificationRequest request)throws Exception;

    void resendVerification(ResendVerificationRequest request) throws  Exception;

    void changePassword(ChangePasswordRequest request) throws ResourceNotFoundException;

    void forgotPassword(ForgotPasswordRequest request) throws ResourceNotFoundException;

    void resetPassword(ResetPasswordRequest request) throws ResourceNotFoundException;

    void changeEmail(ChangeEmailRequest request) throws ResourceNotFoundException;
}
