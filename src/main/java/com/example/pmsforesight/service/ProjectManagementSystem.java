package com.example.pmsforesight.service;

import com.example.pmsforesight.domain.Project;
import com.example.pmsforesight.dto.RequestProjectsDto;
import com.example.pmsforesight.dto.ProjectDto;
import com.example.pmsforesight.dto.DateDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ProjectManagementSystem {

    ResponseEntity<List<Project>> addSubProject(String parentUid, RequestProjectsDto list);

    ResponseEntity<Project> addTask(String parentUid, ProjectDto projectDto);

    ResponseEntity<Project> deleteTaskOrProject(String uid);

    String calculateStatus(String uid, LocalDate date);

    ResponseEntity<Project> updateTaskDates(String uid, DateDto dateDto);

    String projectHierarchy();


}
