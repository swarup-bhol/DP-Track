package com.dptrack.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UserDto {

	private String name;
	private String email;
	private String mobile;
	private String password;
	
}
