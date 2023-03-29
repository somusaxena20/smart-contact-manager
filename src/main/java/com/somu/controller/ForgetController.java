package com.somu.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.somu.dao.UserRepo;
import com.somu.entity.User;
import com.somu.service.EmailService;
import com.somu.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgetController {

	@Autowired
	private UserRepo uRepo;

	@Value("${spring.mail.username}")
	private String sendEmail;

	@Value("${spring.mail.password}")
	private String password;

	@Autowired
	private EmailService emailService;

	Random rand = new Random(10000000);

	@GetMapping("/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}

	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session) {

		User user = uRepo.getUserByEmail(email);
		System.out.println("USER : "+user);
		if (user != null) {

			int otp = rand.nextInt(99999999);

			String subject = "Reset Password OTP Of Smart Contact Manager";

			String message = "<div style='border:1px solid #e2e2e2; padding : 20px'>"
					+ "<h1>Your Smart Contact Manager Reset Password Verification Code : " + otp+"</h1></div>";

			System.out.println("OTP : " + otp);

			System.out.println("SEND EMAIL : " + sendEmail);
			System.out.println("SEND PASSWORD : " + password);

			boolean flag = emailService.sendEmail(sendEmail, password, email, subject, message);

			if (flag == true) {
				session.setAttribute("myotp", otp);
				session.setAttribute("email", email);
				
				return "verify_otp";
			} else {
				return "forgot_email_form";
			}
		} else {
			return "forgot_email_form";
		}
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") String otp, HttpSession session) {
		String myOtp = session.getAttribute("myotp").toString();
		
		
		System.out.println("MYOTP : "+myOtp);
		
		if(otp.equals(myOtp)) {
			System.out.println("OTP MATCHED SUCCESFULLY");
			return "settings";
		}
		else {
			return "home";
		}
		
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session) {
		String email = session.getAttribute("email").toString();
		
		User user = uRepo.getUserByEmail(email);
		
		user.setPassword(newPassword);
		
		uRepo.save(user);
		
		return "home";
	}
	
}
