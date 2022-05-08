package com.example.pmsforesight.service;

import com.example.pmsforesight.dao.ProjectRepository;
import com.example.pmsforesight.domain.Project;
import com.example.pmsforesight.dto.DateDto;
import com.example.pmsforesight.dto.RequestProjectsDto;
import com.example.pmsforesight.dto.ProjectDto;
import com.example.pmsforesight.enums.ProjectType;
import com.example.pmsforesight.exceptions.*;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class ProjectManagementSystemImpl implements ProjectManagementSystem {
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;


    @Autowired
    public ProjectManagementSystemImpl(ProjectRepository projectRepository, ModelMapper modelMapper, Validator validator) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    @Transactional
    public ResponseEntity<List<Project>> addSubProject(String parentUid, RequestProjectsDto body) {
        List<ProjectDto> list = body.getItems();
        if (list.size() != 2 || list.get(0).getType() != ProjectType.PROJECT || list.get(1).getType() != ProjectType.TASK)
            throw new WrongStructureFoundException();
        validator.isValidProject(parentUid, list.get(0), true);
        validator.isValidProject(list.get(0).getUid(), list.get(1), false);

        Project project = modelMapper.map(list.get(0), Project.class);
        setProjectDates(project, modelMapper.map(list.get(1), DateDto.class));
        project.setParentUid(parentUid);
        projectRepository.save(project);
        Project task = addTask(list.get(0).getUid(), list.get(1)).getBody();
        return new ResponseEntity<>(Arrays.asList(project, task), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Project> addTask(String parentUid, ProjectDto projectDto) {
        if (projectDto.getType() != ProjectType.TASK)
            throw new ForbiddenExceptionCustom("Forbidden: you can't add to task");
        validator.isValidProject(parentUid, projectDto, true);
        Project task = modelMapper.map(projectDto, Project.class);
        task.setParentUid(parentUid);
        projectRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Project> deleteTaskOrProject(String uid) {
        Project project = projectRepository.findById(uid).orElseThrow(() -> new ProjectNotFoundException(uid));
        if (project.getType() == ProjectType.TASK && projectRepository.countByTypeEquals(ProjectType.TASK) == 1)
            throw new BadRequestException("Bad request: can't delete last task");

        projectRepository.delete(project);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Override
    public String calculateStatus(String uid, LocalDate date) {
        Project project = projectRepository.findById(uid).orElseThrow(() -> new ProjectNotFoundException(uid));
        LocalDate startDate = project.getStartDate();
        LocalDate endDate = project.getEndDate();
        if (endDate.isBefore(date))
            return "100%";
        if (startDate.isAfter(date))
            return "0%";
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (totalDays == 0) return "100%";
        long pastDays = ChronoUnit.DAYS.between(startDate, date);
        long completion = 100 * pastDays / totalDays;
        return completion + "%";
    }

    @Override
    @Transactional
    public ResponseEntity<Project> updateTaskDates(String uid, DateDto dateDto) {
        if(!dateDto.isValid())
            throw new BadRequestException("Bad request: start date must be before end date");
        Project project = projectRepository.findById(uid).orElseThrow(() -> new ProjectNotFoundException(uid));
        if (project.getType() != ProjectType.TASK)
            throw new BadRequestException("Bad request: you can change dates only for task");
        setProjectDates(project, dateDto);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    private void setProjectDates(Project project, DateDto dateDto) {
        if (dateDto.getStartDate() != null)
            project.setStartDate(dateDto.getStartDate());
        if (dateDto.getEndDate() != null)
            project.setEndDate(dateDto.getEndDate());
    }

    @Override
    public String projectHierarchy() {
        List<Project> projects = projectRepository.findAll();

        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) ->
                        new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .setPrettyPrinting()
                .create()
                .toJson(projects);

    }


}
