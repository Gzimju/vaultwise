package com.vaultwise.service;

import com.vaultwise.model.User;
import com.vaultwise.repository.UserRepository;
import com.vaultwise.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "password123", "john@example.com", "Doe");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        assertFalse(userService.getAllUsers().isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        // Initialize the user object properly with the necessary fields
        User user = new User("John", "password123", "john.doe@example.com", "Doe");
        user.setId(userId);  // Make sure the ID is set

        // Mock the repository to return the user when findById is called
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the method to test
        User foundUser = userService.getUserById(userId);

        // Assert the expected behavior
        assertNotNull(foundUser, "User should not be null");
        assertEquals(userId, foundUser.getId(), "User ID should match");
        assertEquals("John", foundUser.getFirstName(), "First name should match");
        assertEquals("Doe", foundUser.getLastName(), "Last name should match");

        // Verify that the repository method was called
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        User updatedUser = new User("Jane", "newpassword", "jane@example.com", "Smith");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getFirstName(), result.getFirstName());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
