package com.example.pmsforesight.service;

import com.example.pmsforesight.dao.ProjectRepository;
import com.example.pmsforesight.domain.Project;
import com.example.pmsforesight.dto.ProjectDto;
import com.example.pmsforesight.enums.ProjectType;
import com.example.pmsforesight.exceptions.BadRequestException;
import com.example.pmsforesight.exceptions.DuplicatedIdException;
import com.example.pmsforesight.exceptions.ForbiddenExceptionCustom;
import com.example.pmsforesight.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;


public class Validator {
    @Autowired
    private ProjectRepository projectRepository;

    public void isValidProject(String parentUid, ProjectDto projectDto, boolean flag) {
        if (projectDto.getParentUid() != null && !parentUid.equals(projectDto.getParentUid()))
            throw new BadRequestException("Bad request: different parent Ids");
        if (flag) {
            Project parentProject = projectRepository.findById(parentUid).orElseThrow(() -> new ProjectNotFoundException(parentUid));
            if (parentProject.getType() == ProjectType.TASK)
                throw new ForbiddenExceptionCustom("Forbidden: you can't add to task");
        }
        if (projectRepository.existsById(projectDto.getUid()))
            throw new DuplicatedIdException(projectDto.getUid());
    }
}
