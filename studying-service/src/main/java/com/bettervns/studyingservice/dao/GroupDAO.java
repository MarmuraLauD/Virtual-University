package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Group;
import com.bettervns.studyingservice.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Component
public class GroupDAO {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupDAO(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(int id) {
        Optional<Group> groups = groupRepository.findById(id);
        if (groups.isPresent()) {
            return groups.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public Group add(Group group) {
        return groupRepository.save(group);
    }

    public void update(int id, Group updatedGroup) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isPresent()) {
            Group groupi = optionalGroup.get();
            groupi.setName(updatedGroup.getName());
            groupi.setStudyingYear(updatedGroup.getStudyingYear());
            groupi.setDepartment(updatedGroup.getDepartment());
            groupRepository.save(groupi);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        groupRepository.deleteById(id);
    }
}