package com.dptrack.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dptrack.dtos.Login;
import com.dptrack.dtos.UserDto;
import com.dptrack.dtos.VerifyEmail;
import com.dptrack.exceptions.InvalidCredentialException;
import com.dptrack.model.User;
import com.dptrack.service.UserService;
import com.dptrack.utils.DPTrackResponse;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService service;

	
	/**
	 * 
	 * @author swarup-bhol
	 * 
	 * @param user
	 * @return Object
	 */
	@PostMapping("/login")
	public DPTrackResponse<Object> login(@RequestBody Login user) {
		return service.loginUser(user);
	}
	
	
	/****
	 * @author swarup-bhol
	 * 
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/register")
	public DPTrackResponse<Object> register(@RequestBody UserDto user) {
		return service.createNew(user);
	}
	
	
	/**
	 * @author swarup-bhol
	 * 
	 * 
	 * @param file
	 * @param id
	 * @return
	 */
	@PutMapping("/profile-pic/{id}")
	public DPTrackResponse<Object> uploadprofilePic(@RequestParam("file") MultipartFile file, @PathVariable String id) {
		return service.uploadPic(id,file);
	}
	
	
	/**
	 * @author swarup-bhol
	 * 
	 * @param user
	 * @return
	 */
	@PutMapping("/{id}")
	public DPTrackResponse<Object> updateUser(@RequestBody User user, @PathVariable String id) {
		return service.updateUser(user,id);
	}
	
	@PostMapping("/verify")
	public DPTrackResponse<Object> validateOtp(VerifyEmail vemail) throws InvalidCredentialException{
		return service.verifyOtp(vemail);
	}
}
