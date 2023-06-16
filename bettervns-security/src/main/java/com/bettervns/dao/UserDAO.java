package com.bettervns.dao;

import com.bettervns.models.User;
import com.bettervns.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAO {
    private final UserRepository userRepository;

    @Autowired
    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> users = userRepository.findById((long) id);
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> users = userRepository.findByEmail(email);
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    public void update(int id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById((long) id);
        if (optionalUser.isPresent()) {
            User useri = optionalUser.get();
            useri.setId(updatedUser.getId());
            useri.setEmail(updatedUser.getEmail());
            useri.setPassword(updatedUser.getPassword());
            useri.setRole(updatedUser.getRole());
            userRepository.save(useri);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        userRepository.deleteById((long) id);
    }
}