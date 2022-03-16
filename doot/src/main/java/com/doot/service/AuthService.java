package  com.doot.service;

import  com.doot.exception.BadRequestException;
import  com.doot.model.AuthProvider;
import  com.doot.model.ConfirmationToken;
import  com.doot.model.User;
import  com.doot.payload.SignUpRequest;
import  com.doot.repository.ConfirmationTokenRepository;
import  com.doot.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();        
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(Long phoneNumber){
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User saveUser(SignUpRequest signUpRequest) {
        User user = new User();
        Date createdDate = new Date();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setUserId(signUpRequest.getUserId());
        user.setCreatedDate(createdDate);
        user.setLastLogin(createdDate);
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean changePassword(String email, String password) {
        User user = findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if(save(user) != null) {
            return true;
        }
        return false;
    }

    public ConfirmationToken createToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        return confirmationTokenRepository.save(confirmationToken);
    }
    public ConfirmationToken findByConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }
    public void deleteToken(ConfirmationToken confirmationToken) {
        this.confirmationTokenRepository.delete(confirmationToken);
    }
}