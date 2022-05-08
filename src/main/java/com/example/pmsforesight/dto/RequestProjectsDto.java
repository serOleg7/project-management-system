package com.example.pmsforesight.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class RequestProjectsDto {
    @ApiModelProperty(example = " [ {\n" +
            "         \"uid\":\"111\",\n" +
            "         \"name\":\"first\",\n" +
            "         \"type\":\"project\",\n" +
            "         \"startDate\":null,\n" +
            "         \"endDate\":null\n" +
            "      },\n" +
            "      {\n" +
            "         \"uid\":\"222\",\n" +
            "         \"name\":\"second\",\n" +
            "         \"type\":\"task\",\n" +
            "         \"startDate\":\"2022-01-01\",\n" +
            "         \"endDate\":\"2022-12-12\",\n" +
            "         \"parentUid\":\"111\"\n" +
            "      }]")

    private List<ProjectDto> items;
}
