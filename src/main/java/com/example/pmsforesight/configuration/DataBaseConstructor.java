package com.example.pmsforesight.configuration;

import com.example.pmsforesight.dao.ProjectRepository;
import com.example.pmsforesight.domain.Project;
import com.example.pmsforesight.dto.ProjectDto;
import com.example.pmsforesight.dto.JsonDto;
import com.example.pmsforesight.enums.ProjectType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseConstructor implements CommandLineRunner {
    @Autowired private ModelMapper modelMapper;
    @Autowired private ProjectRepository projectRepository;
    @Value("${pathFile.value}") private String pathFile;
    private final Map<String, Project> projects = new HashMap<>();


    @Override
    public void run(String... args) {
        List<ProjectDto> items = getItemsFromJson(pathFile);
        if (items != null) {
            fillProjects(items);
            for (Project project : projects.values()) {
                if (project.getType() == ProjectType.TASK)
                    setProjectDates(projects, project);
            }
            projectRepository.saveAll(projects.values());

        }
    }


    private void setProjectDates(Map<String, Project> projects, Project project) {
        if (project.getParentUid() != null) {
            if (project.getStartDate() == null || projects.get(project.getParentUid()).getStartDate() == null || project.getStartDate().isBefore(projects.get(project.getParentUid()).getStartDate()))
                projects.get(project.getParentUid()).setStartDate(project.getStartDate());
            if (project.getEndDate() == null || projects.get(project.getParentUid()).getEndDate() == null || project.getEndDate().isAfter(projects.get(project.getParentUid()).getEndDate()))
                projects.get(project.getParentUid()).setEndDate(project.getEndDate());

            setProjectDates(projects, projects.get(project.getParentUid()));
        }
    }


    private void fillProjects(List<ProjectDto> items) {
        for (ProjectDto item : items) {
            //TODO add validator
            if (item.getType() == ProjectType.PROJECT || item.getType() == ProjectType.TASK)
                projects.put(item.getUid(), modelMapper.map(item, Project.class));
        }
    }


    private List<ProjectDto> getItemsFromJson(String pathFile) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonDto jsonItems = null;
        try {
            jsonItems = mapper.readValue(new File(pathFile), JsonDto.class);
        } catch (IOException e) {
            System.out.println("Wrong path to file. Data base will not be loaded from file");
        }
        return jsonItems != null ? jsonItems.getItems() : null;
    }
}
