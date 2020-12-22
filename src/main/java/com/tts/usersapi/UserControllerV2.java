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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tts.usersapi.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "users", description = "Operations pertaining to users")
@RestController
@RequestMapping("/v2")
public class UserControllerV2 {
	@Autowired
	private UserRepository userRepository;

	@ApiOperation(value = "Get all users", response = User.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved all users for the given state"),
			@ApiResponse(code = 400, message = "A value for state is required") })
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers(@RequestParam(value = "state", required = true) String state) {
		if (state != null) {
			return new ResponseEntity<>((List<User>) userRepository.findByStateOfResidence(state), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Get user by id", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved user with specified id"),
			@ApiResponse(code = 404, message = "User with specified id does not exist") })
	@GetMapping("/users/{id}")
	public ResponseEntity<Optional<User>> getUserById(@PathVariable(value = "id") Long id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@ApiOperation(value = "Create a new user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created a new user"),
			@ApiResponse(code = 400, message = "You are not authorized to make this request") })
	@PostMapping("/users")
	public ResponseEntity<Void> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// this doesn't work with Postman for some reason :/
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userRepository.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update properties for an existing user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated user information"),
			@ApiResponse(code = 400, message = "There was an error with the request and user was not updated"),
			@ApiResponse(code = 404, message = "Could not find user with specified id") })
	@PutMapping("/users/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable(value = "id") Long id, @RequestBody @Valid User user,
			BindingResult bindingResult) {
		Optional<User> realUser = userRepository.findById(id);
		if (!realUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userRepository.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a user", response = Void.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully deleted user"),
			@ApiResponse(code = 404, message = "User with specified id does not exist") })
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id) {
		Optional<User> realUser = userRepository.findById(id);
		if (!realUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}
		userRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
