package BackendServices.authService.service;

import BackendServices.authService.model.LoginRequest;
import BackendServices.authService.model.LoginResponse;
import BackendServices.authService.util.JwtUtil;
import BackendServices.authService.model.User;
import BackendServices.authService.repository.UserRepository;
import com.google.common.flogger.FluentLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            return "User with this email already exists";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Generate JWT Token
        return jwtUtil.generateToken(savedUser.getUsername(), savedUser.getUserType());
    }

    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginRequest request){
        try{
            User user = userRepository.findByEmail(request.getEmail());
            if(user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())){
                return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(user.getEmail(), user.getUserType())));
            }
        } catch(Exception e){
            logger.atSevere().log("Error = %s occurred while logging in user", e.getMessage());
        }
        return ResponseEntity.badRequest().body(new LoginResponse("Invalid credentials"));    }
}
