package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.*;
import com.bettervns.studyingservice.models.CourseMaterial;
import com.bettervns.studyingservice.models.CourseToGroup;
import com.bettervns.studyingservice.models.StudentAnswer;
import com.bettervns.studyingservice.models.StudentWork;
import com.bettervns.studyingservice.requests.CourseMaterialRequest;
import com.bettervns.studyingservice.requests.StudentWorkRequest;
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
    private final CourseToGroupDAO courseToGroupDAO;
    private final CourseMaterialDAO courseMaterialDAO;
    private final StudentWorkDAO studentWorkDAO;
    private final StudentAnswerDAO studentAnswerDAO;

    @Autowired
    public CoursesController(CourseDAO courseDAO, CourseToGroupDAO courseToGroupDAO,
                             CourseMaterialDAO courseMaterialDAO, StudentWorkDAO studentWorkDAO, StudentAnswerDAO studentAnswerDAO) {
        this.courseDAO = courseDAO;
        this.courseToGroupDAO = courseToGroupDAO;
        this.courseMaterialDAO = courseMaterialDAO;
        this.studentWorkDAO = studentWorkDAO;
        this.studentAnswerDAO = studentAnswerDAO;
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
        if (courseMaterial.getFileName() != null) {
            String directory = "C:\\dev\\files_storage\\" +
                    courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\materials\\";
            String filePath = directory + "\\" + courseMaterial.getFileName();
            File file = new File(filePath);
            if (!file.delete()) System.out.println(("Failed to delete the file attached to material"));
        }
        courseMaterialDAO.delete(materialId);
        return ResponseEntity.ok("Material deleted");
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

    @PostMapping("/course/material_file/{materialId}")
    public ResponseEntity<?> uploadMaterialFile(@RequestParam("file") MultipartFile file, @PathVariable int materialId) {
        try{
            if (courseMaterialDAO.getCourseMaterialsByFileName(file.getOriginalFilename()).size() > 0){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File with this name is already uploaded");
            }
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

    @PatchMapping("/course/material_file/{materialId}")
    public ResponseEntity<?> updateMaterialFile(@RequestParam("file") MultipartFile file, @PathVariable int materialId) {
        List<CourseMaterial> materials = courseMaterialDAO.getCourseMaterialsByFileName(file.getOriginalFilename());
        for (int i = 0; i < materials.size(); i++){
            if (materials.get(i).getId() == materialId) materials.remove(i);
        }
        if (materials.size() > 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File with this name is already uploaded");
        }
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

    @GetMapping("/course/student_work/{workId}")
    public ResponseEntity<?> getStudentWorkById(@PathVariable int workId) {
        return ResponseEntity.ok(new Gson().toJson(studentWorkDAO.getStudentWorkById(workId)));
    }

    @GetMapping("/course/student_works")
    public ResponseEntity<?> getStudentWorks(@RequestParam int courseId, @RequestParam int groupId){
        return ResponseEntity.ok(new Gson().toJson(
                studentWorkDAO.getAllStudentWorksByCourseGroupId(
                        courseToGroupDAO.getByCourseIdAndGroupId(courseId, groupId).getId()
                )
        ));
    }

    @PostMapping("/course/student_works")
    public ResponseEntity<?> addStudentWork(@RequestBody StudentWorkRequest studentWorkRequest){
        CourseToGroup courseToGroup = courseToGroupDAO.getByCourseIdAndGroupId(
                Integer.parseInt(studentWorkRequest.courseId()),
                Integer.parseInt(studentWorkRequest.groupId())
        );
        StudentWork work = new StudentWork(studentWorkRequest.workName(), studentWorkRequest.description(),
                courseToGroup.getId());
        studentWorkDAO.add(work);
        return ResponseEntity.ok("Student work successfully added");
    }

    @PatchMapping("/course/student_work/{workId}")
    public ResponseEntity<?> editStudentWork(@PathVariable int workId, @RequestBody StudentWorkRequest studentWorkRequest){
        StudentWork newWork =  studentWorkDAO.getStudentWorkById(workId);
        newWork.setDescription(studentWorkRequest.description());
        newWork.setName(studentWorkRequest.workName());
        studentWorkDAO.update(workId, newWork);
        return ResponseEntity.ok("Student work successfully edited");
    }

    @DeleteMapping("/course/student_work/{workId}")
    public ResponseEntity<?> deleteStudentWork(@PathVariable int workId) {
        studentWorkDAO.delete(workId);
        return ResponseEntity.ok("Student work successfully deleted");
    }

    @GetMapping("/course/student_works/student_answers")
    public ResponseEntity<?> getStudentAnswersForStudentWork(@RequestParam int studentWorkId ){
        return ResponseEntity.ok(new Gson().toJson(studentAnswerDAO.getAllStudentAnswersByStudentWorkId(studentWorkId)));
    }

    @GetMapping("/course/student_works/student_answer/{studentAnswerId}")
    public ResponseEntity<?> downloadStudentAnswerById(@PathVariable int studentAnswerId){
        StudentAnswer studentAnswer = studentAnswerDAO.getStudentAnswerById(studentAnswerId);
        StudentWork studentWork = studentWorkDAO.getStudentWorkById(studentAnswer.getStudentWorkId());
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(studentWork.getCourseGroupId());
        try {
            String directory = "C:\\dev\\files_storage\\" +
                    courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\student_works\\" +
                    studentWork.getId() + "\\" + studentAnswer.getStudentId() + "\\";
            String filePath = directory + "\\" + studentAnswer.getFileName();
            Resource resource = loadFileAsResource(filePath);
            String contentType = determineContentType(studentAnswer.getFileName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading the file");
        }
    }

    @GetMapping("/course/student_works/student_answer/student")
    public ResponseEntity<?> downloadStudentAnswerByStudentId(@RequestParam int studentId, @RequestParam int studentWorkId){
        StudentAnswer studentAnswer = studentAnswerDAO.getByStudentWorkIdAndStudentId(studentWorkId, studentId);
        StudentWork studentWork = studentWorkDAO.getStudentWorkById(studentAnswer.getStudentWorkId());
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(studentWork.getCourseGroupId());
        try {
            String directory = "C:\\dev\\files_storage\\" +
                    courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\student_works\\" +
                    studentWork.getId() + "\\" + studentAnswer.getStudentId() + "\\";
            String filePath = directory + "\\" + studentAnswer.getFileName();
            Resource resource = loadFileAsResource(filePath);
            String contentType = determineContentType(studentAnswer.getFileName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading the file");
        }
    }

    @PostMapping("/course/student_works/student_answers")
    public ResponseEntity<?> uploadStudentAnswer(@RequestParam("file") MultipartFile file,
                                                   @RequestParam int studentId, @RequestParam int studentWorkId) {
        try {
            StudentWork work = studentWorkDAO.getStudentWorkById(studentWorkId);
            CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(work.getCourseGroupId());
            String fileName = file.getOriginalFilename();
            String dir = "C:\\dev\\files_storage\\" + courseToGroup.getCourseId() + "\\" +
                    courseToGroup.getGroupId() + "\\student_works\\" + studentWorkId + "\\" + studentId + "\\";
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String destPath = dir + fileName;
            studentAnswerDAO.add(new StudentAnswer(fileName, studentId, studentWorkId));
            file.transferTo(new File(destPath));
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file");
        }
    }

    @PatchMapping("/course/student_works/student_answer/{studentAnswerId}")
    public ResponseEntity<?> evaluateStudentAnswer(@PathVariable int studentAnswerId, @RequestParam int mark) {
        StudentAnswer newAnswer = studentAnswerDAO.getStudentAnswerById(studentAnswerId);
        newAnswer.setMark(mark);
        studentAnswerDAO.update(newAnswer.getId(), newAnswer);
        return ResponseEntity.ok("Student answer successfully evaluated");
    }

    @DeleteMapping("/course/student_works/student_answer/{answerId}")
    public ResponseEntity<?> deleteStudentAnswer(@PathVariable int answerId) {
        StudentAnswer answer = studentAnswerDAO.getStudentAnswerById(answerId);
        StudentWork work = studentWorkDAO.getStudentWorkById(answer.getStudentWorkId());
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(work.getCourseGroupId());
        String directory = "C:\\dev\\files_storage\\" + courseToGroup.getCourseId() + "\\" +
                courseToGroup.getGroupId() + "\\student_works\\" + work.getId() + "\\" + answer.getStudentId() + "\\";
        String filePath = directory + "\\" + answer.getFileName();
        File file = new File(filePath);
        if (file.delete()) {
            studentAnswerDAO.delete(answer.getId());
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the file");
        }
    }

    /*
    @GetMapping("/course/student_work_file/{workId}")
    public ResponseEntity<?> downloadStudentWorkFile(@PathVariable int workId) {
        StudentWork studentWork = studentWorkDAO.getStudentWorkById(workId);
        StudentToCourseGroup studentToCourseGroup = studentToCourseGroupDAO.getStudentToCourseGroupById(studentWork.getStudentToCourseGroupId());
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(studentToCourseGroup.getCourseGroupId());
        try {
            String directory = "C:\\dev\\files_storage\\" +
                    courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\student_works\\" +
                    studentToCourseGroup.getStudentId() + "\\";
            String filePath = directory + "\\" + studentWork.getFileName();
            Resource resource = loadFileAsResource(filePath);
            String contentType = determineContentType(studentWork.getFileName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading the file");
        }
    }

    @DeleteMapping("/course/student_work_file/{workId}")
    public ResponseEntity<?> deleteStudentWorkFile(@PathVariable int workId) {
        StudentWork studentWork = studentWorkDAO.getStudentWorkById(workId);
        StudentToCourseGroup studentToCourseGroup = studentToCourseGroupDAO.getStudentToCourseGroupById(studentWork.getStudentToCourseGroupId());
        CourseToGroup courseToGroup = courseToGroupDAO.getCourseToGroupById(studentToCourseGroup.getCourseGroupId());
        String directory = "C:\\dev\\files_storage\\" +
                courseToGroup.getCourseId() + "\\" + courseToGroup.getGroupId() + "\\student_works\\" +
                studentToCourseGroup.getStudentId() + "\\";
        String filePath = directory + "\\" + studentWork.getFileName();
        File file = new File(filePath);
        if (file.delete()) {
            StudentWork newWork = studentWorkDAO.getStudentWorkById(workId);
            newWork.setFileName(null);
            studentWorkDAO.update(workId, newWork);
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the file");
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