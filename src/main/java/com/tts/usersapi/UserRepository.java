package com.tts.usersapi;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.usersapi.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByStateOfResidence(String state);

	Optional<User> findById(Long id);
}
