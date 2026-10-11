package com.example.userservice.service;

import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

    @Test
    void create_normalizesEmail() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.create("Alice", "  Alice@Example.COM  ", 30);

        assertThat(created.getName()).isEqualTo("Alice");
        assertThat(created.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findById_notFound_throws() {
        when(userRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(7L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void update_changesFields() {
        User existing = new User("Old", "old@x.com", 30);
        existing.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = userService.update(1L, "New", null, 35);

        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getEmail()).isEqualTo("old@x.com");
        assertThat(updated.getAge()).isEqualTo(35);
    }

    @Test
    void delete_notFound_throws() {
        when(userRepository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(5L))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, never()).deleteById(any());
    }
}