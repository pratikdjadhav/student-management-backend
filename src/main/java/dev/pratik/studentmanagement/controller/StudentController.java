package dev.pratik.studentmanagement.controller;

import dev.pratik.studentmanagement.dto.StudentRequest;
import dev.pratik.studentmanagement.dto.StudentResponse;
import dev.pratik.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.addStudent(request));
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(studentService.getAllStudents(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                studentService.getStudentsByCourse(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(
                studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully!");
    }

    // NEW — Total collected fees
    @GetMapping("/fees/collected")
    public ResponseEntity<Double> getTotalCollectedFees() {
        return ResponseEntity.ok(studentService.getTotalCollectedFees());
    }

    // NEW — Total pending fees
    @GetMapping("/fees/pending")
    public ResponseEntity<Double> getTotalPendingFees() {
        return ResponseEntity.ok(studentService.getTotalPendingFees());
    }
}