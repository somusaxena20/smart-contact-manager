package com.somu.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.somu.entity.Contact;
import com.somu.entity.User;

public interface ContactRepo extends JpaRepository<Contact, Integer> {
	
	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactsByUser(@Param ("userId") Integer userId, Pageable pageable);
	
	public List<Contact> findByNameContainingAndUser(String name, User user);
}
