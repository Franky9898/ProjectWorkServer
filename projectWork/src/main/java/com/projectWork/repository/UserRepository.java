package com.projectWork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projectWork.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{	
	Optional<User> findByEmail(String email);
	Optional<User> findByToken(String token);
}
