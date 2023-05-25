package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Course;
import com.bettervns.studyingservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Component
public class CourseDAO {

    private final CourseRepository CourseRepository;

    @Autowired
    public CourseDAO(CourseRepository CourseRepository) {
        this.CourseRepository = CourseRepository;
    }

    public List<Course> getAllCourses() {
        return CourseRepository.findAll();
    }

    public Course getCourseById(int id) {
        Optional<Course> courses = CourseRepository.findById(id);
        if (courses.isPresent()) {
            return courses.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public Course add(Course course) {
        return CourseRepository.save(course);
    }

    public void update(int id, Course updatedCourse) {
        Optional<Course> optionalCourse = CourseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course coursei = optionalCourse.get();
            coursei.setName(updatedCourse.getName());
            coursei.setDepartment(updatedCourse.getDepartment());
            coursei.setTeacher(updatedCourse.getTeacher());
            CourseRepository.save(coursei);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        CourseRepository.deleteById(id);
    }
}