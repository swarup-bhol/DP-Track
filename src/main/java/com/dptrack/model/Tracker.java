package com.dptrack.model;

import java.sql.Timestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tracker {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	private String trackerId;
	@NotEmpty
	private String imei;
	@NotEmpty
	private String name;
	@NotEmpty
	private String veichleNo;
	
	private String userId;
	
	@CreationTimestamp
	private Timestamp created;
	
	@UpdateTimestamp
	private Timestamp updated;
	
	private boolean status;
	
}
