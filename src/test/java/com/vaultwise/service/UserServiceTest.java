package com.vaultwise.service;

import com.vaultwise.model.User;
import com.vaultwise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("John", "password123", "john.doe@example.com", "Doe");
    }

    @Test
    void createUser_ShouldReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {
        User user1 = new User("John", "password123", "john.doe@example.com", "Doe");
        User user2 = new User("Jane", "password123", "jane.doe@example.com", "Doe");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirstName());
    }

    @Test
    void getUserById_ShouldReturnNullIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User foundUser = userService.getUserById(1L);

        assertNull(foundUser);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
