package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.*;
import com.bettervns.studyingservice.models.CourseMaterial;
import com.bettervns.studyingservice.models.CourseToGroup;
import com.bettervns.studyingservice.requests.CourseMaterialRequest;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/studying")
public class CoursesController {
    private final CourseDAO courseDAO;
    private final GroupDAO groupDAO;
    private final StudentToCourseGroupDAO studentToCourseGroupDAO;
    private final CourseToGroupDAO courseToGroupDAO;
    private final CourseMaterialDAO courseMaterialDAO;

    @Autowired
    public CoursesController(CourseDAO courseDAO, GroupDAO groupDAO, StudentToCourseGroupDAO studentToCourseGroupDAO,
                             CourseToGroupDAO courseToGroupDAO, CourseMaterialDAO courseMaterialDAO) {
        this.courseDAO = courseDAO;
        this.groupDAO = groupDAO;
        this.studentToCourseGroupDAO = studentToCourseGroupDAO;
        this.courseToGroupDAO = courseToGroupDAO;
        this.courseMaterialDAO = courseMaterialDAO;
    }

    @GetMapping("/courses")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(courseDAO.getAllCourses()));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id){
        return ResponseEntity.ok(new Gson().toJson(courseDAO.getCourseById(id)));
    }

    @GetMapping("/course/materials")
    public ResponseEntity<?> getCourseMaterials(@RequestParam int courseId, @RequestParam int groupId){
        List<CourseToGroup> courseToGroups = courseToGroupDAO.getAllCourseToGroups();
        CourseToGroup courseToGroup = null;
        for (CourseToGroup i : courseToGroups){
            if (i.getCourseId() == courseId && i.getGroupId() == groupId){
                courseToGroup = i;
                break;
            }
        }
        List<CourseMaterial> allMaterials = courseMaterialDAO.getAllCourseMaterials();
        List<CourseMaterial> courseGroupMaterials = new ArrayList<>();
        for (CourseMaterial i : allMaterials){
            if (i.getCourseToGroupId() == courseToGroup.getId()){
                courseGroupMaterials.add(i);
            }
        }
        return ResponseEntity.ok(new Gson().toJson(courseGroupMaterials));
    }

    @GetMapping("/course/material/{materialId}")
    public ResponseEntity<?> getCourseMaterialById(@PathVariable int materialId) {
        courseMaterialDAO.getCourseMaterialById(materialId);
        return ResponseEntity.ok(new Gson().toJson(courseMaterialDAO.getCourseMaterialById(materialId)));
    }

    @PostMapping("/course/materials")
    public ResponseEntity<?> addCourseMaterial(@RequestBody CourseMaterialRequest courseMaterialRequest){
        List<CourseToGroup> courseToGroups = courseToGroupDAO.getAllCourseToGroups();
        CourseToGroup courseToGroup = null;
        for (CourseToGroup i : courseToGroups){
            if (i.getCourseId() == Integer.parseInt(courseMaterialRequest.courseId()) &&
                    i.getGroupId() == Integer.parseInt(courseMaterialRequest.groupId())){
                courseToGroup = i;
                break;
            }
        }
        courseMaterialDAO.add(new CourseMaterial(courseMaterialRequest.materialName(),
                courseMaterialRequest.description(), courseToGroup.getId()));
        return ResponseEntity.ok("Material added");
    }

    @PatchMapping("/course/material/{materialId}")
    public ResponseEntity<?> updateCourseMaterial(@PathVariable int materialId, @RequestBody CourseMaterialRequest courseMaterialRequest){
        List<CourseToGroup> courseToGroups = courseToGroupDAO.getAllCourseToGroups();
        CourseToGroup courseToGroup = null;
        for (CourseToGroup i : courseToGroups){
            if (i.getCourseId() == Integer.parseInt(courseMaterialRequest.courseId()) &&
                    i.getGroupId() == Integer.parseInt(courseMaterialRequest.groupId())){
                courseToGroup = i;
                break;
            }
        }
        CourseMaterial newMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
        newMaterial.setDescription(courseMaterialRequest.description());
        newMaterial.setCourseToGroupId(courseToGroup.getId());
        newMaterial.setName(courseMaterialRequest.materialName());
        courseMaterialDAO.update(materialId, newMaterial);
        return ResponseEntity.ok("Material updated");
    }

    @DeleteMapping("/course/material/{materialId}")
    public ResponseEntity<?> deleteCourseMaterial(@PathVariable int materialId) {
        CourseMaterial courseMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(
                courseMaterialDAO.getCourseMaterialById(materialId).getCourseToGroupId());
        String directory = "C:\\dev\\files_storage\\" +
                courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\materials\\";
        String filePath = directory + "\\" + courseMaterial.getFileName();
        File file = new File(filePath);
        if (!file.delete()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the file attached to material");
        courseMaterialDAO.delete(materialId);
        return ResponseEntity.ok("Material deleted");
    }

    @PostMapping("/course/material_file/{materialId}")
    public ResponseEntity<?> uploadMaterialFile(@RequestParam("file") MultipartFile file, @PathVariable int materialId) {
        try{
            CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(
                    courseMaterialDAO.getCourseMaterialById(materialId).getCourseToGroupId());
            String fileName = file.getOriginalFilename();
            String dir = "C:\\dev\\files_storage\\" + courseToGroup.getCourseId() + "\\" +
                    courseToGroup.getGroupId() + "\\materials\\";
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String destPath = dir + fileName;
            CourseMaterial newMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
            newMaterial.setFileName(fileName);
            courseMaterialDAO.update(materialId, newMaterial);
            file.transferTo(new File(destPath));
            return ResponseEntity.ok("File uploaded successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file");
        }
    }

    @DeleteMapping("/course/material_file/{materialId}")
    public ResponseEntity<?> deleteMaterialFile(@PathVariable int materialId) {
        CourseMaterial courseMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(
                courseMaterialDAO.getCourseMaterialById(materialId).getCourseToGroupId());
        String directory = "C:\\dev\\files_storage\\" +
                courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\materials\\";
        String filePath = directory + "\\" + courseMaterial.getFileName();
        File file = new File(filePath);
        if (file.delete()) {
            CourseMaterial newMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
            newMaterial.setFileName(null);
            courseMaterialDAO.update(materialId, newMaterial);
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the file");
        }
    }

    @PatchMapping("/course/material_file/{materialId}")
    public ResponseEntity<?> updateMaterialFile(@RequestParam("file") MultipartFile file, @PathVariable int materialId) {
        CourseMaterial courseMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(
                courseMaterialDAO.getCourseMaterialById(materialId).getCourseToGroupId());
        String directory = "C:\\dev\\files_storage\\" +
                courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\materials\\";
        String filePath = directory + "\\" + courseMaterial.getFileName();
        File oldFile = new File(filePath);
        if (oldFile.delete()) {
            try{
                String fileName = file.getOriginalFilename();
                String destPath = "C:\\dev\\files_storage\\" + courseToGroup.getCourseId() + "\\" +
                        courseToGroup.getGroupId() + "\\materials\\" + fileName;
                CourseMaterial newMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
                newMaterial.setFileName(fileName);
                courseMaterialDAO.update(materialId, newMaterial);
                file.transferTo(new File(destPath));
                return ResponseEntity.ok("File updated successfully");
            }
            catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating new file");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to deleting old the file");
        }
    }

    @GetMapping("/course/material_file/{materialId}")
    public ResponseEntity<?> downloadMaterialFile(@PathVariable int materialId) {
        CourseMaterial courseMaterial = courseMaterialDAO.getCourseMaterialById(materialId);
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(
                courseMaterialDAO.getCourseMaterialById(materialId).getCourseToGroupId());
        try {
            String directory = "C:\\dev\\files_storage\\" +
                    courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\materials\\";
            String filePath = directory + "\\" + courseMaterial.getFileName();
            Resource resource = loadFileAsResource(filePath);
            String contentType = determineContentType(courseMaterial.getFileName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading the file");
        }
    }

    /*@PostMapping("/course/upload/work")
    public ResponseEntity<?> uploadStudentWork(@RequestParam("file") MultipartFile file, @RequestParam int studentId,
                                               @RequestParam int groupId, @RequestParam int courseId) {
        try{
            String fileName = file.getOriginalFilename();
            String destPath = "C:\\dev\\files_storage\\" +
                    courseDAO.getCourseById(courseId).getName() + "\\" + groupDAO.getGroupById(groupId).getName() +
                    "\\student_works\\" + fileName;
            file.transferTo(new File(destPath));
            return ResponseEntity.ok("File uploaded successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file");
        }
    }*/

    /*@GetMapping("/course/download/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, @RequestParam int groupId, @RequestParam int courseId) {
        try {
            String directory = "C:\\dev\\files_storage\\" +
                    courseDAO.getCourseById(courseId).getName() + "\\" + groupDAO.getGroupById(groupId).getName() +"\\";
            String filePath = directory + "\\" + filename;
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
    }*/

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
        System.out.println(111);
        return ResponseEntity.ok(new Gson().toJson(courseDAO.getCoursesForGroup(groupId)));
    }

    @GetMapping("/courses/teacher/{teacherId}")
    public ResponseEntity<?> getCoursesForTeacher(@PathVariable("teacherId") int teacherId){
        System.out.println(111);
        return ResponseEntity.ok(new Gson().toJson(courseDAO.getCoursesForTeacher(teacherId)));
    }
}