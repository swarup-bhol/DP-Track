package com.dptrack.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class CommonService {
 
	public String generateCode() {
		Random rnd = new Random();
		int code = rnd.nextInt(9999);
		return String.format("%04d", code);
	}
}
