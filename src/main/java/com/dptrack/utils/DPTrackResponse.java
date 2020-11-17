package com.dptrack.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DPTrackResponse<T> {

	private int statusCode;
	private boolean status ;
	private String message;
	private Object result;
}
