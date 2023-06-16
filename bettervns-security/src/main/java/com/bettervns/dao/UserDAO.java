package com.bettervns.dao;


import com.bettervns.models.RefreshToken;
import com.bettervns.models.User;
import com.bettervns.repository.RefreshTokenRepository;
import com.bettervns.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDAO {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EntityManager entityManager;

    @Autowired
    public UserDAO(UserRepository userRepository, EntityManager entityManager, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.refreshTokenRepository = refreshTokenRepository;
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
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e){
            System.out.println(e.getMessage());
            System.out.println("No users with email: " + email);
            return null;
        }
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    public void update(User updatedUser) {
        Optional<User> optionalUser = userRepository.findByEmail(updatedUser.getEmail());
        if (optionalUser.isPresent()) {
            User useri = optionalUser.get();
            useri.setEmail(updatedUser.getEmail());
            useri.setPassword(updatedUser.getPassword());
            useri.setRole(updatedUser.getRole());
            userRepository.delete(optionalUser.get());
            userRepository.save(useri);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        userRepository.deleteById((long) id);
    }

    public void deleteByEmail(String email){
        if(getUserByEmail(email) != null) userRepository.delete(getUserByEmail(email));
    }
}