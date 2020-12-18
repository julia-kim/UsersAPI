package com.tts.usersapi;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tts.usersapi.model.User;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers(@RequestParam(value = "state", required = false) String state) {
		if (state != null) {
			return new ResponseEntity<>((List<User>) userRepository.findByStateOfResidence(state), HttpStatus.OK);
		}
		return new ResponseEntity<>((List<User>) userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<Optional<User>> getUserById(@PathVariable(value = "id") Long id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<Void> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userRepository.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable(value = "id") Long id, @RequestBody @Valid User user,
			BindingResult bindingResult) {
		if (userRepository.findById(id) == null) {
			bindingResult.rejectValue("id", "error.id", "User with given ID does not exist");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userRepository.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id) {
		if (userRepository.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
