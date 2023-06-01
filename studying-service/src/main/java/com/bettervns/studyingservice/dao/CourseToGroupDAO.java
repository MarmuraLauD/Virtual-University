package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.CourseToGroup;
import com.bettervns.studyingservice.repository.CourseToGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class CourseToGroupDAO {

    private final CourseToGroupRepository courseToGroupRepository;

    @Autowired
    public CourseToGroupDAO(com.bettervns.studyingservice.repository.CourseToGroupRepository courseToGroupRepository) {
        this.courseToGroupRepository = courseToGroupRepository;
    }

    public List<CourseToGroup> getAllCourseToGroups() {
        return courseToGroupRepository.findAll();
    }

    public CourseToGroup getCourseToGroupById(int id) {
        Optional<CourseToGroup> courseToGroups = courseToGroupRepository.findById(id);
        if (courseToGroups.isPresent()) {
            return courseToGroups.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public CourseToGroup add(CourseToGroup courseToGroup) {
        return courseToGroupRepository.save(courseToGroup);
    }

    public void update(int id, CourseToGroup updatedCourseToGroup) {
        Optional<CourseToGroup> optionalCourseToGroup = courseToGroupRepository.findById(id);
        if (optionalCourseToGroup.isPresent()) {
            CourseToGroup courseToGroupi = optionalCourseToGroup.get();
            courseToGroupi.setCourseId(updatedCourseToGroup.getCourseId());
            courseToGroupi.setGroupId(updatedCourseToGroup.getGroupId());
            courseToGroupRepository.save(courseToGroupi);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        courseToGroupRepository.deleteById(id);
    }
}