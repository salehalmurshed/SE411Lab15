package edu.psu.se411.lab15.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.psu.se411.lab15.exception.ObjectNotFoundException;
import edu.psu.se411.lab15.model.Course;
import edu.psu.se411.lab15.repository.CourseRepository;

@Service
public class CourseService {

	private CourseRepository courseRepository;
	
	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}
	
	public Iterable<Course> getAllCourses() {
		return courseRepository.findAll();
	}
	
	public Optional<Course> getCourseById(Long id) {
		return courseRepository.findById(id);
	}

	public Course createCourse(Course course) {
		return courseRepository.save(course);
	}

	public void updateCourse(Long id, Course course) {
		Course existingCourse = courseRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Course not found"));
		System.out.println("Updating course: " + existingCourse.toString());
		Course updatedCourse = new Course(id, course.code(), course.name(), course.description(), course.credits());
		courseRepository.save(updatedCourse);
	}

	public void deleteCourse(Long id) {
		Course existingCourse = courseRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Course not found"));
		
		courseRepository.deleteById(id);
	}
	
}
