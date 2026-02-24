package com.imgify.controller;

import com.imgify.model.User;
import com.imgify.service.ImageService;
import com.imgify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/image")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateImage(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String prompt = request.get("prompt").toString();

            User user = userService.getUserById(userId);
            if (user.getCredits() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No credits left"));
            }

            String base64Image = imageService.generateImage(prompt);

            // Deduct credit
            userService.updateUserCredits(userId, user.getCredits() - 1);

            return ResponseEntity.ok(Map.of("success", true, "image", base64Image, "credits", user.getCredits() - 1));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
