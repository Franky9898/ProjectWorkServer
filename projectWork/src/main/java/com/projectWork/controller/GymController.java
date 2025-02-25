package com.projectWork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.model.Gym;
import com.projectWork.repository.GymRepository;

@RestController
@RequestMapping("/gyms")
@CrossOrigin(origins = {})
public class GymController
{
	@Autowired
	private GymRepository gymRepository;

	@GetMapping
	public ResponseEntity<Object> getAllGyms()
	{
		String result;
		List<Gym> gyms = gymRepository.findAll();
		if (gyms.isEmpty())
		{
			result = "Non ci sono palestre disponibili";
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.ok(gyms);
	}
}
