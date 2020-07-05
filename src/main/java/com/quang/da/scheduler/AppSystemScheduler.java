package com.quang.da.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.quang.da.service.ProblemRequestService;

@Component
public class AppSystemScheduler {
	
	@Autowired
	ProblemRequestService service;
	
	
	@Scheduled(cron = "0 5 0 * * *")
	public void collectExpireRequest() {
		System.out.println("Start Collect");
		service.deactiveExpireRequest();
	}
}
