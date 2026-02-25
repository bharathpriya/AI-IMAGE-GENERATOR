package com.imgify.controller;

import com.imgify.model.User;
import com.imgify.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        logger.info("Received register request for email: {}", user.getEmail());
        try {
            User registeredUser = userService.registerUser(user);
            logger.info("User registered successfully: {}", registeredUser.getEmail());
            return ResponseEntity.ok(Map.of("success", true, "user", registeredUser));
        } catch (Exception e) {
            logger.error("Registration failed for email {}: {}", user.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        logger.info("Received login request for email: {}", email);
        try {
            User user = userService.loginUser(email, credentials.get("password"));
            logger.info("Login successful for user: {}", email);
            return ResponseEntity.ok(Map.of("success", true, "user", user));
        } catch (Exception e) {
            logger.error("Login failed for email {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/credits")
    public ResponseEntity<?> getCredits(@RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(Map.of("success", true, "credits", user.getCredits()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/add-credits")
    public ResponseEntity<?> addCredits(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            int credits = Integer.parseInt(request.get("credits").toString());
            User user = userService.getUserById(userId);
            User updatedUser = userService.updateUserCredits(userId, user.getCredits() + credits);
            return ResponseEntity.ok(Map.of("success", true, "credits", updatedUser.getCredits()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
