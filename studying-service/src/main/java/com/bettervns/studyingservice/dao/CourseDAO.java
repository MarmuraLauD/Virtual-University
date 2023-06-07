package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.AppointmentToGroup;
import com.bettervns.studyingservice.models.Course;
import com.bettervns.studyingservice.models.CourseToGroup;
import com.bettervns.studyingservice.models.Group;
import com.bettervns.studyingservice.repository.CourseRepository;
import com.bettervns.studyingservice.repository.CourseToGroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CourseDAO {

    private final CourseRepository courseRepository;

    private final CourseToGroupDAO courseToGroupDAO;
    private final EntityManager entityManager;

    @Autowired
    public CourseDAO(CourseRepository courseRepository, EntityManager entityManager, CourseToGroupDAO courseToGroupDAO) {
        this.courseRepository = courseRepository;
        this.entityManager = entityManager;
        this.courseToGroupDAO = courseToGroupDAO;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int id) {
        Optional<Course> courses = courseRepository.findById(id);
        if (courses.isPresent()) {
            return courses.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public List<Course> getCoursesForGroup(int groupId){
        List<Integer> coursesIDs = new ArrayList<>();
        List<CourseToGroup> courseToGroups = courseToGroupDAO.getCourseToGroupByGroupId(groupId);
        for (CourseToGroup i : courseToGroups){
            if (!coursesIDs.contains(i.getCourseId())) coursesIDs.add(i.getCourseId());
        }
        List<Course> resultList = new ArrayList<>();
        for (Integer i: coursesIDs){
            resultList.add(getCourseById(i));
        }
        return resultList;
    }

    public Course add(Course course) {
        return courseRepository.save(course);
    }

    public void update(int id, Course updatedCourse) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course coursei = optionalCourse.get();
            coursei.setName(updatedCourse.getName());
            coursei.setDepartmentId(updatedCourse.getDepartmentId());
            coursei.setTeacherId(updatedCourse.getTeacherId());
            courseRepository.save(coursei);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        courseRepository.deleteById(id);
    }
}