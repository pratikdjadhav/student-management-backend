package dev.pratik.studentmanagement.service;

import dev.pratik.studentmanagement.dto.StudentRequest;
import dev.pratik.studentmanagement.dto.StudentResponse;
import dev.pratik.studentmanagement.exception.ResourceNotFoundException;
import dev.pratik.studentmanagement.model.Course;
import dev.pratik.studentmanagement.model.Student;
import dev.pratik.studentmanagement.model.StudentStatus;
import dev.pratik.studentmanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseService courseService;

    // Convert Student → StudentResponse
    private StudentResponse mapToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setPhone(student.getPhone());
        response.setAddress(student.getAddress());
        response.setPaidFees(student.getPaidFees() != null ?
                student.getPaidFees() : 0.0);
        response.setEnrollmentDate(student.getEnrollmentDate());
        response.setStatus(student.getStatus() != null ?
                student.getStatus().name() : "ACTIVE");

        if (student.getCourse() != null) {
            response.setCourseName(student.getCourse().getName());
            response.setCourseId(student.getCourse().getId());
            Double courseFees = student.getCourse().getFees() != null ?
                    student.getCourse().getFees() : 0.0;
            response.setCourseFees(courseFees);
            response.setPendingFees(courseFees - response.getPaidFees());
        }
        return response;
    }

    // Convert StudentRequest → Student
    private Student mapToEntity(StudentRequest request) {
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());
        student.setPaidFees(request.getPaidFees() != null ?
                request.getPaidFees() : 0.0);
        student.setEnrollmentDate(request.getEnrollmentDate() != null ?
                request.getEnrollmentDate() : LocalDate.now());
        student.setStatus(request.getStatus() != null ?
                StudentStatus.valueOf(request.getStatus()) : StudentStatus.ACTIVE);

        if (request.getCourseId() != null) {
            Course course = courseService.getCourseById(request.getCourseId());
            student.setCourse(course);
        }
        return student;
    }

    // Add student
    public StudentResponse addStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }


        if (request.getCourseId() != null && request.getPaidFees() != null) {
            Course course = courseService.getCourseById(request.getCourseId());
            if (request.getPaidFees() > course.getFees()) {
                throw new RuntimeException(
                        "Paid fees cannot be more than course fees of ₹"
                                + course.getFees());
            }
        }

        Student student = mapToEntity(request);
        return mapToResponse(studentRepository.save(student));
    }

    // Get all students with pagination
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // Get student by ID
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with id " + id + " not found"));
        return mapToResponse(student);
    }

    // Get students by course
    public List<StudentResponse> getStudentsByCourse(Long courseId) {
        return studentRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update student
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with id " + id + " not found"));
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());
        existing.setAddress(request.getAddress());
        existing.setPaidFees(request.getPaidFees() != null ?
                request.getPaidFees() : existing.getPaidFees());
        if (request.getEnrollmentDate() != null) {
            existing.setEnrollmentDate(request.getEnrollmentDate());
        }
        if (request.getStatus() != null) {
            existing.setStatus(StudentStatus.valueOf(request.getStatus()));
        }
        if (request.getCourseId() != null) {
            Course course = courseService.getCourseById(request.getCourseId());
            existing.setCourse(course);
        }
        if (request.getCourseId() != null && request.getPaidFees() != null) {
            Course course = courseService.getCourseById(request.getCourseId());
            if (request.getPaidFees() > course.getFees()) {
                throw new RuntimeException(
                        "Paid fees cannot be more than course fees of ₹"
                                + course.getFees());
            }
        }
        return mapToResponse(studentRepository.save(existing));
    }

    // Delete student
    public void deleteStudent(Long id) {
        studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with id " + id + " not found"));
        studentRepository.deleteById(id);
    }

    // Get total collected fees
    public Double getTotalCollectedFees() {
        return studentRepository.findAll()
                .stream()
                .mapToDouble(s -> s.getPaidFees() != null ?
                        s.getPaidFees() : 0.0)
                .sum();
    }

    // Get total pending fees
    public Double getTotalPendingFees() {
        return studentRepository.findAll()
                .stream()
                .mapToDouble(s -> {
                    double courseFees = s.getCourse() != null &&
                            s.getCourse().getFees() != null ?
                            s.getCourse().getFees() : 0.0;
                    double paidFees = s.getPaidFees() != null ?
                            s.getPaidFees() : 0.0;
                    return courseFees - paidFees;
                })
                .sum();
    }
}