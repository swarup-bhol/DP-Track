package com.dptrack.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter  @NoArgsConstructor @AllArgsConstructor @Builder
public class Login {

	private String email;
	private String password;
	
}
