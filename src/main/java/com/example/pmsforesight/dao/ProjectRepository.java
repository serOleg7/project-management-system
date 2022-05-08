package com.example.pmsforesight.dao;

import com.example.pmsforesight.domain.Project;
import com.example.pmsforesight.enums.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {

    Integer countByTypeEquals(ProjectType type);
}
