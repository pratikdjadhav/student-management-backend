package dev.pratik.studentmanagement.service;

import dev.pratik.studentmanagement.exception.ResourceNotFoundException;
import dev.pratik.studentmanagement.model.Course;
import dev.pratik.studentmanagement.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // Add course
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get course by ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course with id " + id + " not found"));
    }

    // Update course
    public Course updateCourse(Long id, Course updatedCourse) {
        Course existing = getCourseById(id);
        existing.setName(updatedCourse.getName());
        existing.setDuration(updatedCourse.getDuration());
        existing.setDescription(updatedCourse.getDescription());
        existing.setFees(updatedCourse.getFees()); // ← ADD THIS
        return courseRepository.save(existing);
    }

    // Delete course
    public void deleteCourse(Long id) {
        getCourseById(id);
        courseRepository.deleteById(id);
    }
}