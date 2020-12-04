package com.dptrack.service.impl;

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

import com.dptrack.dtos.Login;
import com.dptrack.dtos.UserDto;
import com.dptrack.dtos.VerifyEmail;
import com.dptrack.exceptions.EmailNotSentException;
import com.dptrack.exceptions.InvalidCredentialException;
import com.dptrack.exceptions.UserNotFound;
import com.dptrack.model.User;
import com.dptrack.repository.UserRepository;
import com.dptrack.service.UserService;
import com.dptrack.utils.CommonService;
import com.dptrack.utils.Constants;
import com.dptrack.utils.DPTrackResponse;
import com.dptrack.utils.EmailService;
import com.dptrack.utils.JwtToken;
import com.dptrack.utils.JwtTokenUtil;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

//	public static final String uploadingDir = System.getProperty("user.dir") + "/Uploads/ProfilePic/";

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRepository userDao;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	JwtTokenUtil jwtToken;

	@Autowired
	EmailService emailService;

	@Autowired
	CommonService cs;

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

					if (!existuingUser.isVerified()) {
						response = new DPTrackResponse<>(200, true, Constants.VERIFY_ACCOUNT, existuingUser);
					} else {
						JwtToken token = JwtToken.builder().userId(existuingUser.getUserId())
								.token(jwtToken.generateToken(existuingUser)).build();
						response = new DPTrackResponse<>(200, true, HttpStatus.OK.name(), token);
					}
				} else {
					response = new DPTrackResponse<>(400, true, Constants.INVALID_CREDENTIALS, user);
				}
			}

		} catch (Exception e) {
			log.error("Exception : loginUser", e.getMessage());
			return new DPTrackResponse<>(417, false, Constants.FIELD_MISSING, null);
		}

		return response;
	}

	@Override
	public DPTrackResponse<Object> createNew(UserDto user) {
		DPTrackResponse<Object> response = null;
		try {
			User newuser = userDao.findByEmail(user.getEmail());
			if (newuser == null) {
				User build = User.builder().email(user.getEmail()).fullName(user.getName())
						.userId(UUID.randomUUID().toString()).password(encoder.encode(user.getPassword())).status(true)
						.build();
				String code = cs.generateCode();
				boolean sentMail = emailService.sentMail(user.getEmail(), code, "DpTrack",
						"Your DpTrack otp for verifying email is :");
				if (sentMail) {
					response = new DPTrackResponse<>(201, true, HttpStatus.CREATED.name(), userDao.save(build));
				} else {
					response = new DPTrackResponse<>(400, true, HttpStatus.BAD_REQUEST.name(), user);
				}

			} else {
				response = new DPTrackResponse<>(409, false, Constants.EXIST, user);
			}
		} catch (IllegalArgumentException e) {
			log.error("Exception :createNew", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.FIELD_MISSING, user);
		} catch (EmailNotSentException e) {
			log.error("Inavalid Email :createNew", e.getMessage());
			return new DPTrackResponse<>(400, false, Constants.INVALID_EMAIL, user);
		} catch (Exception e) {
			log.error("Exception :createNew", e.getMessage());
			return new DPTrackResponse<>(500, false, Constants.EXCEPTIONS, user);
		}

		return response;
	}

	@Override
	public DPTrackResponse<Object> uploadPic(String id, MultipartFile file) {
		DPTrackResponse<Object> response = null;
//		try {
//			User user = userDao.findByUserId(id);
//			if (user == null)
//				response = new DPTrackResponse<>(404, false, HttpStatus.NOT_FOUND.name(), id);
//			if (!new File(uploadingDir).exists()) {
//				new File(uploadingDir).mkdirs();
//			}
//			String uniqueID = UUID.randomUUID().toString();
//			Path uploadPath = Paths.get(uploadingDir, uniqueID + file.getOriginalFilename());
//			Files.write(uploadPath, file.getBytes());
//			if (user.getPath() != null) {
//				Path prevPath = Paths.get(user.getPath());
//				Files.deleteIfExists(prevPath);
//			}
//			user.setPath(uploadPath.toString());
//			response = new DPTrackResponse<>(200, true, Constants.OK, userDao.save(user));
//		} catch (IOException e) {
//			log.error("IOException :uploadPic", e.getMessage());
//			return new DPTrackResponse<>(409, false, Constants.EXCEPTIONS, null);
//		} catch (Exception e) {
//			log.error("Exception :uploadPic", e.getMessage());
//			return new DPTrackResponse<>(409, false, Constants.EXCEPTIONS, null);
//		}
		return response;
	}

	@Override
	public DPTrackResponse<Object> updateUser(UserDto user, String id) {
		DPTrackResponse<Object> response = null;
		try {
			User findUser = userDao.findByUserId(id);
			if (user == null)
				response = new DPTrackResponse<>(404, false, HttpStatus.NOT_FOUND.name(), id);
			findUser.setFullName(user.getName());
			response = new DPTrackResponse<>(200, true, Constants.OK, userDao.save(findUser));
		} catch (Exception e) {
			log.error("Exception :updateUser", e.getMessage());
			return new DPTrackResponse<>(200, false, Constants.EXCEPTIONS, null);
		}
		return response;
	}

	@Override
	public DPTrackResponse<Object> verifyOtp(VerifyEmail vemail) {
 		DPTrackResponse<Object> response = null;
		try {

			if (vemail.getEmail() == null || vemail.getOtp() == null) {
				throw new InvalidCredentialException("Invalid Credentials");
			}
			User user = userDao.findByEmail(vemail.getEmail());
			if (user == null)
				throw new UserNotFound("Email not found");

			if (user.getEmailVerifCode().equals(vemail.getOtp())) {
				user.setVerified(true);
				userDao.save(user);
				response = new DPTrackResponse<>(200, true, HttpStatus.OK.name(), jwtToken.generateToken(user));
			} else {
				response = new DPTrackResponse<>(400, false, HttpStatus.BAD_REQUEST.name(), vemail);
			}
		} catch (InvalidCredentialException e) {
			log.error("InvalidCredentialException :verifyOtp", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.FIELD_MISSING, vemail);
		} catch (IllegalArgumentException e) {
			log.error("Exception :verifyOtp", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.FIELD_MISSING, vemail);
		}catch (UserNotFound e) {
			log.error("User Not found exception :verifyOtp", e.getMessage());
			return new DPTrackResponse<>(409, false, Constants.USER_NOT_FOUND, vemail);
		}
		catch (Exception e) {
			log.error("Exception :verifyOtp", e.getMessage());
			response = new DPTrackResponse<>(500, false, HttpStatus.INTERNAL_SERVER_ERROR.name(), vemail);
		}
		return response;
	}

	@Override
	public DPTrackResponse<Object> sendOtp(String email) {
		DPTrackResponse<Object> response = null;
		try {
			User user = userDao.findByEmail(email);
			if (user == null)
				throw new UserNotFound(Constants.USER_NOT_FOUND);
			String code = cs.generateCode();
			boolean sentMail = emailService.sentMail(user.getEmail(), code, "DpTrack",
					"Your DpTrack otp for verifying email is :");
			if (sentMail) {
				user.setEmailVerifCode(code);
				response = new DPTrackResponse<>(200, true, Constants.OTP_SENT, userDao.save(user));
			} else {
				response = new DPTrackResponse<>(400, true, HttpStatus.BAD_REQUEST.name(), user);
			}
		} catch (IllegalArgumentException e) {
			log.error("Illegal args exceptions: sendOtp");
		} catch (EmailNotSentException e) {
			log.error("Inavalid Email :sendOtp", e.getMessage());
			return new DPTrackResponse<>(400, false, Constants.INVALID_EMAIL, email);
		} catch (Exception e) {
			log.error("Exception : sendOtp");
		}
		return response;
	}

}
