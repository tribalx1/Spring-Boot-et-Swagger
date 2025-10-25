package com.example.student_management.controller;

import com.example.student_management.entity.Student;
import com.example.student_management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Créer un étudiant")
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student saved = studentService.save(student);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Lister tous les étudiants")
    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @Operation(summary = "Récupérer un étudiant par identifiant")
    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Mettre à jour un étudiant")
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable int id, @RequestBody Student student) {
        student.setId(id);
        Student updated = studentService.save(student);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer un étudiant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = studentService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Compter le nombre total d'étudiants")
    @GetMapping("/count")
    public long count() {
        return studentService.countStudents();
    }

    @Operation(summary = "Statistiques du nombre d'étudiants par année de naissance")
    @GetMapping("/stats/by-year")
    public List<Map<String, Object>> statsByYear() {
        Collection<Object[]> rows = studentService.findNbrStudentByYear();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("year", row[0]);
            map.put("count", row[1]);
            result.add(map);
        }
        return result;
    }
}