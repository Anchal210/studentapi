
package com.example.studentapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // 1. Add Student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return repository.save(student);
    }

    // 2. Get All Students
    @GetMapping
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // 3. Search Student By ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Update Scores and Skills
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        return repository.findById(id).map(existingStudent -> {

            existingStudent.setScore(student.getScore());
            existingStudent.setSkills(student.getSkills());

            return ResponseEntity.ok(repository.save(existingStudent));

        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Student deleted successfully");
        }

        return ResponseEntity.notFound().build();
    }

    // 6. Merit List
    @GetMapping("/merit-list")
    public List<Student> getMeritList() {
        return repository.findAllByOrderByScoreDesc();
    }
}
