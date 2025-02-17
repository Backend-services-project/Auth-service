package BackendServices.authService.controller;

import BackendServices.authService.model.LoginRequest;
import BackendServices.authService.service.UserService;
import BackendServices.authService.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        String token = userService.registerUser(user);

        // Return token in response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

    }
}
