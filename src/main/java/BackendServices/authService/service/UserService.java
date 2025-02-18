package BackendServices.authService.service;

import BackendServices.authService.model.LoginRequest;
import BackendServices.authService.model.LoginResponse;
import BackendServices.authService.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    String registerUser(User user);

    ResponseEntity<LoginResponse> loginUser(LoginRequest request);
}
