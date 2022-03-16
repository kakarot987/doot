package com.doot;

import com.doot.models.User;
import com.doot.payload.*;
import com.doot.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		if (loginRequest.getEmail() == null) {
			Optional<User> user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber());
			JwtResponse jwtResponse = authService.updateUserAuth(user.get().getUserId(),
					loginRequest.getPassword());
			return ResponseEntity.ok(jwtResponse);
		}
		if (loginRequest.getPhoneNumber() == null) {
			System.out.println("very through email");
			User user = userRepository.findByEmail(loginRequest.getEmail());
			JwtResponse jwtResponse = authService.updateUserAuth(user.getUserId(),
					loginRequest.getPassword());
			return ResponseEntity.ok(jwtResponse);
		}
		return ResponseEntity.badRequest().body("Email or Phone is not correct");
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println("-------" + signUpRequest.getPassword() + "---" +
				signUpRequest.getUserId());

		String username = String.valueOf(signUpRequest.getUserId());

		System.out.println(username);
		//return if userId is already registered
		if (userRepository.existsByUserId(signUpRequest.getUserId())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: userId is already in use!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Phone Number is already in use!"));
		}

		System.out.println("Authorizing userId and password");

		//Setup Registration Date
		Date createdDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		// Create new user's account
		User user = new User(signUpRequest.getUserId(),
				signUpRequest.getEmail(),
				signUpRequest.getPhoneNumber(),
				"jwt",
				encoder.encode(signUpRequest.getPassword()),
				createdDate);
		userRepository.save(user);
		return ResponseEntity.ok("User Register Successfully");
	}

	// Display forgotPassword page
	@RequestMapping(value = "/forgot", method = RequestMethod.GET)
	public ModelAndView displayForgotPasswordPage() {
		return new ModelAndView("forgotPassword");
	}

	// Process form submission from forgotPassword page
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ModelAndView processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String userEmail, HttpServletRequest request) {

		// Lookup user in database by e-mail
		Optional<User> optional = userService.findUserByEmail(userEmail);

		if (!optional.isPresent()) {
			modelAndView.addObject("errorMessage", "We didn't find an account for that e-mail address.");
		} else {

			// Generate random 36-character string token for reset password
			User user = optional.get();
			user.setResetToken(UUID.randomUUID().toString());

			// Save token to database
			userService.saveUser(user);

			String appUrl = request.getScheme() + "://" + request.getServerName();

			// Email message
			SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
			passwordResetEmail.setFrom("support@demo.com");
			passwordResetEmail.setTo(user.getEmail());
			passwordResetEmail.setSubject("Password Reset Request");
			passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
					+ "/reset?token=" + user.getResetToken());

			emailService.sendEmail(passwordResetEmail);

			// Add success message to view
			modelAndView.addObject("successMessage", "A password reset link has been sent to " + userEmail);
		}

		modelAndView.setViewName("forgotPassword");
		return modelAndView;

	}

	// Display form to reset password
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token) {

		Optional<User> user = userService.findUserByResetToken(token);

		if (user.isPresent()) { // Token found in DB
			modelAndView.addObject("resetToken", token);
		} else { // Token not found in DB
			modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
		}

		modelAndView.setViewName("resetPassword");
		return modelAndView;
	}

	// Process reset password form
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

		// Find the user associated with the reset token
		Optional<User> user = userService.findUserByResetToken(requestParams.get("token"));

		// This should always be non-null but we check just in case
		if (user.isPresent()) {

			User resetUser = user.get();

			// Set new password
			resetUser.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));

			// Set the reset token to null so it cannot be used again
			resetUser.setResetToken(null);

			// Save user
			userService.saveUser(resetUser);

			// In order to set a model attribute on a redirect, we must use
			// RedirectAttributes
			redir.addFlashAttribute("successMessage", "You have successfully reset your password.  You may now login.");

			modelAndView.setViewName("redirect:login");
			return modelAndView;

		} else {
			modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
			modelAndView.setViewName("resetPassword");
		}

		return modelAndView;
	}

	// Going to reset page without a token redirects to login page
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
		return new ModelAndView("redirect:login");
	}
}
}
