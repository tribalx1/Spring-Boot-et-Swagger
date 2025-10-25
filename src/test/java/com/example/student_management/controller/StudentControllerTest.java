package com.example.student_management.controller;

import com.example.student_management.entity.Student;
import com.example.student_management.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testCreateStudent() {
        Student input = new Student();
        input.setNom("Mido");
        when(studentService.save(any(Student.class))).thenAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            s.setId(1);
            return s;
        });

        ResponseEntity<Student> response = studentController.create(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("Mido", response.getBody().getNom());
        verify(studentService, times(1)).save(any(Student.class));
    }

    @Test
    void testDeleteStudent_success() {
        when(studentService.delete(1)).thenReturn(true);
        ResponseEntity<Void> response = studentController.delete(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService, times(1)).delete(1);
    }

    @Test
    void testDeleteStudent_notFound() {
        when(studentService.delete(99)).thenReturn(false);
        ResponseEntity<Void> response = studentController.delete(99);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(studentService, times(1)).delete(99);
    }

    @Test
    void testFindAllStudents() {
        Student s1 = new Student();
        Student s2 = new Student();
        when(studentService.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Student> result = studentController.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentService, times(1)).findAll();
    }

    @Test
    void testCountStudents() {
        when(studentService.countStudents()).thenReturn(10L);
        long count = studentController.count();
        assertEquals(10L, count);
        verify(studentService, times(1)).countStudents();
    }

    @Test
    void testStatsByYear() {
        Collection<Object[]> rows = Arrays.asList(
                new Object[]{1985, 2L},
                new Object[]{1990, 3L}
        );
        when(studentService.findNbrStudentByYear()).thenReturn(rows);

        List<Map<String, Object>> result = studentController.statsByYear();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1985, result.get(0).get("year"));
        assertEquals(2L, result.get(0).get("count"));
        assertEquals(1990, result.get(1).get("year"));
        assertEquals(3L, result.get(1).get("count"));
        verify(studentService, times(1)).findNbrStudentByYear();
    }
}