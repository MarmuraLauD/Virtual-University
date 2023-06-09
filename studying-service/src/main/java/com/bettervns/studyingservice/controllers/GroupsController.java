package com.bettervns.studyingservice.controllers;

import com.bettervns.studyingservice.dao.CourseToGroupDAO;
import com.bettervns.studyingservice.dao.GroupDAO;
import com.bettervns.studyingservice.models.CourseToGroup;
import com.bettervns.studyingservice.models.Group;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/studying")
public class GroupsController {
    private final GroupDAO groupDAO;
    private final CourseToGroupDAO courseToGroupDAO;

    @Autowired
    public GroupsController(GroupDAO groupDAO, CourseToGroupDAO courseToGroupDAO) {
        this.groupDAO = groupDAO;
        this.courseToGroupDAO = courseToGroupDAO;
    }

    @GetMapping("/groups")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new Gson().toJson(groupDAO.getAllGroups()));
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<?> show(@PathVariable("id") int id){
        return ResponseEntity.ok(new Gson().toJson(groupDAO.getGroupById(id)));
    }

    @GetMapping("/groups/{departmentId}")
    public ResponseEntity<?> getGroupsByDepartmentId(@PathVariable("departmentId") int departmentId){
        return ResponseEntity.ok(new Gson().toJson(groupDAO.getGroupsByDepartmentId(departmentId)));
    }

    @GetMapping("/groups/course/{courseId}")
    public ResponseEntity<?> getGroupsRegisteredOnCourse(@PathVariable("courseId") int courseId){
        List<CourseToGroup> courseToGroups = courseToGroupDAO.getCourseToGroupsByCourseId(courseId);
        List<Group> groups = new ArrayList<>();
        for (CourseToGroup i : courseToGroups){
            groups.add(groupDAO.getGroupById(i.getGroupId()));
        }
        return ResponseEntity.ok(new Gson().toJson(groups));
    }

}