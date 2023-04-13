package com.example1.stream.service;

import com.example1.stream.model.message.Contract;

public interface SmokeTestKafkaSendService { 

	void sendSmokeTest(
		Contract contract
	) ; 

}