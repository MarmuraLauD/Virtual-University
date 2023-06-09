package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.CourseMaterial;
import com.bettervns.studyingservice.repository.CourseAttachedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class CourseMaterialDAO {

    private final CourseAttachedFileRepository CourseAttachedFileRepository;

    @Autowired
    public CourseMaterialDAO(CourseAttachedFileRepository courseAttachedFileRepository) {
        this.CourseAttachedFileRepository = courseAttachedFileRepository;
    }

    public List<CourseMaterial> getAllCourseMaterials() {
        return CourseAttachedFileRepository.findAll();
    }

    public CourseMaterial getCourseMaterialById(int id) {
        Optional<CourseMaterial> courseAttachedFiles = CourseAttachedFileRepository.findById(id);
        if (courseAttachedFiles.isPresent()) {
            return courseAttachedFiles.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public CourseMaterial add(CourseMaterial courseMaterial) {
        return CourseAttachedFileRepository.save(courseMaterial);
    }

    public void update(int id, CourseMaterial updatedCourseMaterial) {
        Optional<CourseMaterial> optionalCourseAttachedFile = CourseAttachedFileRepository.findById(id);
        if (optionalCourseAttachedFile.isPresent()) {
            CourseMaterial courseAttachedFilei = optionalCourseAttachedFile.get();
            courseAttachedFilei.setName(updatedCourseMaterial.getName());
            courseAttachedFilei.setDescription(updatedCourseMaterial.getDescription());
            courseAttachedFilei.setFileName(updatedCourseMaterial.getFileName());
            courseAttachedFilei.setCourseToGroupId(updatedCourseMaterial.getCourseToGroupId());
            CourseAttachedFileRepository.save(courseAttachedFilei);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        CourseAttachedFileRepository.deleteById(id);
    }
}