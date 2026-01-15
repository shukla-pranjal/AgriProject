package com.farmflow.service;

import com.farmflow.dto.UserDTO;
import com.farmflow.entity.User;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.impl.UserServiceImpl;
import com.farmflow.util.Constants;
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.service.AuthService;
import com.farmflow.util.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Validation validation;

    @Mock
    private AuthService authService;

    @Mock
    private EmailComposerService emailComposerService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");

        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getName(), result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(authService).validateUser(1);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1);
        });

        String expectedMessage = String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, 1);
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
