package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.CourseAttachedFile;
import com.bettervns.studyingservice.repository.CourseAttachedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class CourseAttachedFileDAO {

    private final CourseAttachedFileRepository CourseAttachedFileRepository;

    @Autowired
    public CourseAttachedFileDAO(CourseAttachedFileRepository courseAttachedFileRepository) {
        this.CourseAttachedFileRepository = courseAttachedFileRepository;
    }

    public List<CourseAttachedFile> getAllCourseAttachedFiles() {
        return CourseAttachedFileRepository.findAll();
    }

    public CourseAttachedFile getCourseAttachedFileById(int id) {
        Optional<CourseAttachedFile> courseAttachedFiles = CourseAttachedFileRepository.findById(id);
        if (courseAttachedFiles.isPresent()) {
            return courseAttachedFiles.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public CourseAttachedFile add(CourseAttachedFile courseAttachedFile) {
        return CourseAttachedFileRepository.save(courseAttachedFile);
    }

    public void update(int id, CourseAttachedFile updatedCourseAttachedFile) {
        Optional<CourseAttachedFile> optionalCourseAttachedFile = CourseAttachedFileRepository.findById(id);
        if (optionalCourseAttachedFile.isPresent()) {
            CourseAttachedFile courseAttachedFilei = optionalCourseAttachedFile.get();
            courseAttachedFilei.setName(updatedCourseAttachedFile.getName());
            courseAttachedFilei.setFileLink(updatedCourseAttachedFile.getFileLink());
            courseAttachedFilei.setCourseToGroup(updatedCourseAttachedFile.getCourseToGroup());
            CourseAttachedFileRepository.save(courseAttachedFilei);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        CourseAttachedFileRepository.deleteById(id);
    }
}