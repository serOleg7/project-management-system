package com.example.pmsforesight.dto;

import com.example.pmsforesight.enums.ProjectType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProjectDto {
    private String uid;
    private String name;
    @ApiModelProperty(example = "TASK")
    private ProjectType type;
    @JsonFormat(pattern = "yyyy-MM-dd")

    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")

    private LocalDate endDate;
    @ApiModelProperty(hidden = true)
    private String parentUid;

}
