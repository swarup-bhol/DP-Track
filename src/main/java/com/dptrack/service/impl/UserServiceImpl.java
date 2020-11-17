package com.dptrack.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dptrack.dao.UserDao;
import com.dptrack.dtos.Login;
import com.dptrack.model.User;
import com.dptrack.service.UserService;
import com.dptrack.utils.Constants;
import com.dptrack.utils.DPTrackResponse;
import com.dptrack.utils.JwtToken;
import com.dptrack.utils.JwtTokenUtil;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	public static final String uploadingDir = System.getProperty("user.dir") + "/Uploads/ProfilePic/";

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserDao userDao;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	JwtTokenUtil jwtToken;

	@Override
	public UserDetails loadUserByUsername(String username) {
		org.springframework.security.core.userdetails.User loginUser = null;
		try {

			User user = userDao.findByEmail(username);
			if (user == null) {
				loginUser = null;
			} else {
				loginUser = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
						getAutority());
			}

		} catch (Exception e) {
			log.error("Exceptions :loadUserByUsername", e.getMessage());
			return null;
		}
		return loginUser;
	}

	private List<SimpleGrantedAuthority> getAutority() {
		return Arrays.asList(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public DPTrackResponse<Object> loginUser(Login user) {

		User existuingUser = null;
		DPTrackResponse<Object> response = null;
		try {
			existuingUser = userDao.findByEmail(user.getEmail());
			if (existuingUser == null) {
				response = new DPTrackResponse<>(404, false, HttpStatus.NOT_FOUND.name(), null);
			} else {
				boolean check = encoder.matches(user.getPassword(), existuingUser.getPassword());
				if (check) {
					JwtToken token = JwtToken.builder().userId(existuingUser.getUserId()).token(jwtToken.generateToken(existuingUser)).build();
					response = new DPTrackResponse<>(200, true, HttpStatus.OK.name(),token);							
				} else {
					response = new DPTrackResponse<>(400, true, Constants.INVALID_CREDENTIALS,
							jwtToken.generateToken(existuingUser));
				}
			}

		} catch (Exception e) {
			log.error("Exception : loginUser", e.getMessage());
			return new DPTrackResponse<>(417, false, Constants.FIELD_MISSING, null);
		}

		return response;
	}

	@Override
	public DPTrackResponse<Object> createNew(User user) {
		DPTrackResponse<Object> response = null;
		try {
			User newuser = userDao.findByEmail(user.getEmail());
			if (newuser == null) {
				User build = User.builder().email(user.getEmail()).fullName(user.getFullName())
						.userId(UUID.randomUUID().toString()).password(encoder.encode(user.getPassword())).status(true)
						.build();
				user = userDao.save(build);
				response = new DPTrackResponse<>(200, true, HttpStatus.OK.name(), user);
			} else {
				response = new DPTrackResponse<>(409, false, Constants.EXIST, user);
			}
		} catch (NullPointerException e) {
			log.error("Exception :createNew", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.FIELD_MISSING, user);
		} catch (Exception e) {
			log.error("Exception :createNew", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.EXCEPTIONS, user);
		}

		return response;
	}

	@Override
	public DPTrackResponse<Object> uploadPic(String id, MultipartFile file) {
		DPTrackResponse<Object> response = null;
		try {
			User user = userDao.findByUserId(id);
			if (user == null)
				response = new DPTrackResponse<>(404, false, HttpStatus.NOT_FOUND.name(), id);
			if (!new File(uploadingDir).exists()) {
				new File(uploadingDir).mkdirs();
			}
			String uniqueID = UUID.randomUUID().toString();
			Path uploadPath = Paths.get(uploadingDir, uniqueID + file.getOriginalFilename());
			Files.write(uploadPath, file.getBytes());
			if (user.getPath() != null) {
				Path prevPath = Paths.get(user.getPath());
				Files.deleteIfExists(prevPath);
			}
			user.setPath(uploadPath.toString());
			response = new DPTrackResponse<>(200, true, Constants.OK, userDao.save(user));
		} catch (IOException e) {
			log.error("IOException :uploadPic", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.EXCEPTIONS, null);
		} catch (Exception e) {
			log.error("Exception :uploadPic", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.EXCEPTIONS, null);
		}
		return response;
	}

	@Override
	public DPTrackResponse<Object> updateUser(User user, String id) {
		DPTrackResponse<Object> response = null;
		try {
			User findUser = userDao.findByUserId(id);
			if (user == null)
				response = new DPTrackResponse<>(404, false, HttpStatus.NOT_FOUND.name(), id);
			findUser.setFullName(user.getFullName());
			response = new DPTrackResponse<>(200, true, Constants.OK, userDao.save(findUser));
		} catch (Exception e) {
			log.error("Exception :updateUser", e.getMessage());
			return new DPTrackResponse<>(200, false, Constants.EXCEPTIONS, null);
		}
		return response;
	}

}
