package com.dptrack.service;

import org.springframework.web.multipart.MultipartFile;

import com.dptrack.dtos.Login;
import com.dptrack.dtos.UserDto;
import com.dptrack.dtos.VerifyEmail;
import com.dptrack.utils.DPTrackResponse;

public interface UserService {
	
//	public UserDetails loadUserByUsername(String username);

	public DPTrackResponse<Object> loginUser(Login user);

	public DPTrackResponse<Object> createNew(UserDto user);

	public DPTrackResponse<Object> uploadPic(String id, MultipartFile file);

	public DPTrackResponse<Object> updateUser(UserDto user, String id);

	public DPTrackResponse<Object> verifyOtp(VerifyEmail vemail);

	public DPTrackResponse<Object> sendOtp(String email);

}
