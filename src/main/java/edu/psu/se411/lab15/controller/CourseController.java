package edu.psu.se411.lab15.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import edu.psu.se411.lab15.exception.ObjectNotFoundException;
import edu.psu.se411.lab15.model.Course;
import edu.psu.se411.lab15.service.CourseService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
	
	private CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}
	
	@GetMapping()
	public ResponseEntity<Iterable<Course>> getAllCourses() {
		return ResponseEntity.ok(courseService.getAllCourses());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
		Optional<Course> op = courseService.getCourseById(id);
		if(op.isPresent()) {
			return ResponseEntity.ok(op.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping()
	public ResponseEntity<Void> createCourse(@Valid @RequestBody Course course, UriComponentsBuilder ucb) {
		if(course.id() != null) {
			return ResponseEntity.badRequest().build();
		}
		Course createdCourse = courseService.createCourse(course);
		URI uri = ucb.path("/api/v1/courses/{id}").buildAndExpand(createdCourse.id()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateCourse(@PathVariable @Positive Long id, @Valid @RequestBody Course course) {
		if(course.id() != null) {
			return ResponseEntity.badRequest().build();
		}
		try {
			courseService.updateCourse(id, course);
			return ResponseEntity.noContent().build();
		} catch (ObjectNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
		try {
			courseService.deleteCourse(id);
			return ResponseEntity.noContent().build();
		} catch (ObjectNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	
}
