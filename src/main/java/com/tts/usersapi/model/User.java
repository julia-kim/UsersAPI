package com.tts.usersapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "The auto-generated id of the user")
	private Long id;
	@Size(max=20)
	@ApiModelProperty(notes = "The first name of the user")
	private String firstName;
	@Size(min=2)
	@ApiModelProperty(notes = "The last name of the user")
	private String lastName;
	@Size(min=4, max=20)
	@ApiModelProperty(notes = "The (US) state of residence of the user")
	private String stateOfResidence;
}