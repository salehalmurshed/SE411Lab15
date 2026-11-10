package edu.psu.se411.lab15.model;

import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record Course(
			@Id Long id,
			@NotBlank(message = "Course code cannot be empty")
			String code,
			@NotBlank(message = "Name cannot be empty")
			@Size(min = 2, message = "Name cannot be shorter than two characters")
			String name,
			String description,
			@Min(value = 1, message = "Credits should be from 1 to 10")
			@Max(value = 10, message = "Credits should be from 1 to 10")
			Integer credits
			
		) {}
