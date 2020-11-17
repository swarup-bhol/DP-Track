package com.dptrack.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
	
	@Id
	@GeneratedValue
	@JsonIgnore
	private long id;
	@Column(unique = true)
	private String userId;
	
	private String fullName;
	@Column(unique = true)
	@Email
	@NotEmpty
	private String email;
	
	@NotEmpty @JsonIgnore
	private String password;
	@JsonIgnore
	private String path;
	@JsonIgnore
	private String emailVerifCode;
	@JsonIgnore
	private String resetCode;
	private boolean status;
}
