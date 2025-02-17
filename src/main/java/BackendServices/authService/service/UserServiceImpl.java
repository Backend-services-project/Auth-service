package BackendServices.authService.service;

import BackendServices.authService.util.JwtUtil;
import BackendServices.authService.model.User;
import BackendServices.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
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
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        User savedUser = userRepository.save(user);

        // Generate JWT Token
        return jwtUtil.generateToken(savedUser.getUsername(), savedUser.getUserType());
    }
}
