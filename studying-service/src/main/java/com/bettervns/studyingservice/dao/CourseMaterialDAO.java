package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.CourseMaterial;
import com.bettervns.studyingservice.repository.CourseMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class CourseMaterialDAO {

    private final CourseMaterialRepository courseMaterialRepository;

    @Autowired
    public CourseMaterialDAO(CourseMaterialRepository courseMaterialRepository) {
        this.courseMaterialRepository = courseMaterialRepository;
    }

    public List<CourseMaterial> getAllCourseMaterials() {
        return courseMaterialRepository.findAll();
    }

    public CourseMaterial getCourseMaterialById(int id) {
        Optional<CourseMaterial> courseAttachedFiles = courseMaterialRepository.findById(id);
        if (courseAttachedFiles.isPresent()) {
            return courseAttachedFiles.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public List<CourseMaterial> getCourseMaterialsByFileName(String filename) {
        return courseMaterialRepository.findByFileName(filename);
    }

    public CourseMaterial add(CourseMaterial courseMaterial) {
        return courseMaterialRepository.save(courseMaterial);
    }

    public void update(int id, CourseMaterial updatedCourseMaterial) {
        Optional<CourseMaterial> optionalCourseAttachedFile = courseMaterialRepository.findById(id);
        if (optionalCourseAttachedFile.isPresent()) {
            CourseMaterial courseAttachedFilei = optionalCourseAttachedFile.get();
            courseAttachedFilei.setName(updatedCourseMaterial.getName());
            courseAttachedFilei.setDescription(updatedCourseMaterial.getDescription());
            courseAttachedFilei.setFileName(updatedCourseMaterial.getFileName());
            courseAttachedFilei.setCourseToGroupId(updatedCourseMaterial.getCourseToGroupId());
            courseMaterialRepository.save(courseAttachedFilei);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        courseMaterialRepository.deleteById(id);
    }
}