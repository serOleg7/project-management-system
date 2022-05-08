package com.example.pmsforesight.controller;

import com.example.pmsforesight.configuration.SwaggerConfiguration;
import com.example.pmsforesight.domain.Project;
import com.example.pmsforesight.dto.DateDto;
import com.example.pmsforesight.dto.RequestProjectsDto;
import com.example.pmsforesight.dto.ProjectDto;
import com.example.pmsforesight.service.ProjectManagementSystem;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("api/v1")
@Api(tags = {SwaggerConfiguration.DESCRIPTION})
public class ProjectManagementController {
    final private ProjectManagementSystem pmsService;


    @Autowired
    public ProjectManagementController(ProjectManagementSystem pmsService) {
        this.pmsService = pmsService;
    }


    @ApiOperation(value = "Add new project with provided task by parent id", notes = "Return project and task",
            response = com.example.pmsforesight.dto.RequestProjectsDto.class ,responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request: different parent Ids"),
            @ApiResponse(code = 403, message = "Forbidden: you can't add to task"),
            @ApiResponse(code = 404, message = "Not found project with #Id"),
            @ApiResponse(code = 409, message = "Conflict: you can't add task with the same id. Task #Id already exists"),
            @ApiResponse(code = 415, message = "Unsupported Media Type: wrong request body"),
    })
    @PostMapping("/post/project/{parentUid}")
    public ResponseEntity<List<Project>> addSubProject(@PathVariable @ApiParam(example = "690ajgop520") String parentUid, @RequestBody RequestProjectsDto body) {
        return pmsService.addSubProject(parentUid, body);
    }


    @ApiOperation(value = "Add new task by parent id", notes = "Return a task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task created"),
            @ApiResponse(code = 400, message = "Bad request: different parent Ids"),
            @ApiResponse(code = 403, message = "Forbidden: you can't add to task"),
            @ApiResponse(code = 404, message = "Not found project with #Id"),
            @ApiResponse(code = 409, message = "Conflict: you can't add task with the same id. Task #Id already exists")
    })
    @PostMapping("/post/task/{parentUid}")
    public ResponseEntity<Project> addTask(@PathVariable @ApiParam(example = "690ajgop520") String parentUid, @RequestBody ProjectDto task) {
        return pmsService.addTask(parentUid, task);
    }


    @ApiOperation(value = "Delete task or project", notes = "Return a deleted task or project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request: can't delete last task"),
            @ApiResponse(code = 404, message = "Not found project with #Id")
    })
    @DeleteMapping("/delete/task/{uid}")
    public ResponseEntity<Project> deleteTaskOrProject(@PathVariable @ApiParam(example = "291egdzr615") String uid) {
        return pmsService.deleteTaskOrProject(uid);
    }


    @ApiOperation(value = "Calculate completion status of project or task", notes = "Return string %")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request: wrong format of date"),
            @ApiResponse(code = 404, message = "Not found project with #Id")
    })
    @GetMapping("/get/status/{uid}")
    public String calculateStatus(@PathVariable @ApiParam(example = "301nmutw647") String uid, @RequestParam
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiParam(example = "2022-05-05") LocalDate date) {
        return pmsService.calculateStatus(uid, date);
    }


    @ApiOperation(value = "Update task's start/end date", notes = "Return a updated task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request: you can change dates only for task | Bad request: start date must be before end date"),
            @ApiResponse(code = 404, message = "Not found project with #Id")
    })
    @PutMapping("/update/task/date/{uid}")
    public ResponseEntity<Project> updateTaskDates(@PathVariable @ApiParam(example = "221wgjag761") String uid, @RequestBody DateDto dates) {
        return pmsService.updateTaskDates(uid, dates);
    }


    @ApiOperation(value = "Return all projects as json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal server error: something went wrong")
    })
    @GetMapping("/get/hierarchy")
    public String projectHierarchy() {
        return pmsService.projectHierarchy();
    }

}
