package com.quang.da.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quang.da.entity.Major;
import com.quang.da.repository.MajorRepository;

@Service
public class MajorServiceImpl implements MajorService {

	@Autowired
	private MajorRepository rep;
	
	
	@Override
	public List<Major> getAllMajor() {
		return rep.findAll();
	}
	
}
