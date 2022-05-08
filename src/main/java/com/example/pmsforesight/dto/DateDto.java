package com.example.pmsforesight.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DateDto {
    @ApiModelProperty(example = "2022-05-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(example = "2022-05-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;




    public boolean isValid() {
        if(startDate != null && endDate != null)
            return endDate.isAfter(startDate);

        else return true;
    }

}