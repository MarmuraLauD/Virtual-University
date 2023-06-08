package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.CourseDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/studying")
public class CoursesController {
    private final CourseDAO courseDAO;

    @Autowired
    public CoursesController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @GetMapping("/courses")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(courseDAO.getAllCourses()));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id){
        return ResponseEntity.ok(new Gson().toJson(courseDAO.getCourseById(id)));
    }

    //TODO: Make normal file requesting logic!

    @PostMapping("/course/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try{
            String fileName = file.getOriginalFilename();
            String destPath = "C:\\dev\\files_storage\\" + fileName;
            file.transferTo(new File(destPath));
            return ResponseEntity.ok("File uploaded successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file");
        }
    }

    @GetMapping("/course/download/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) {
        try {
            String directory = "C:\\dev\\files_storage\\";
            String filePath = directory + "/" + filename;
            Resource resource = loadFileAsResource(filePath);
            String contentType = determineContentType(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading the file");
        }
    }

    private Resource loadFileAsResource(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        Resource resource = new org.springframework.core.io.UrlResource(file.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found: " + filePath);
        }
    }

    private String determineContentType(String filename) {
        String fileExtension = filename.substring(filename.indexOf('.') + 1);
        System.out.println(fileExtension);
        return "application/" + fileExtension;
    }

    @GetMapping("/courses/group/{groupId}")
    public ResponseEntity<?> getCoursesForGroup(@PathVariable("groupId") int groupId){
        return ResponseEntity.ok(new Gson().toJson(courseDAO.getCoursesForGroup(groupId)));
    }
}