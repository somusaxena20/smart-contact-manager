package com.somu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.somu.dao.UserRepo;
import com.somu.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepo repo;
	
	public User save(User u)
	{
		repo.save(u);
		return u;
	}
}
