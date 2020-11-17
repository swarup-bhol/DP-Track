package com.dptrack.service;

import org.springframework.web.multipart.MultipartFile;

import com.dptrack.dtos.Login;
import com.dptrack.model.User;
import com.dptrack.utils.DPTrackResponse;

public interface UserService {
	
//	public UserDetails loadUserByUsername(String username);

	public DPTrackResponse<Object> loginUser(Login user);

	public DPTrackResponse<Object> createNew(User user);

	public DPTrackResponse<Object> uploadPic(String id, MultipartFile file);

	public DPTrackResponse<Object> updateUser(User user, String id);

}
