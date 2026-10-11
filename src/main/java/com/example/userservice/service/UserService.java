package com.example.userservice.service;

import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User create(String name, String email, Integer age) {
        User user = new User(name.trim(), email.trim().toLowerCase(), age);
        User saved = userRepository.save(user);
        log.info("Создан пользователь id={}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User update(Long id, String name, String email, Integer age) {
        User existing = findById(id);

        if (name != null && !name.isBlank()) {
            existing.setName(name.trim());
        }
        if (email != null && !email.isBlank()) {
            existing.setEmail(email.trim().toLowerCase());
        }
        if (age != null) {
            existing.setAge(age);
        }

        User updated = userRepository.save(existing);
        log.info("Обновлён пользователь id={}", updated.getId());
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Удалён пользователь id={}", id);
    }
}