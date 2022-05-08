package com.example.pmsforesight.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.EnumUtils;

public enum ProjectType {
    PROJECT,
    TASK;

    @JsonCreator
    public static ProjectType getCurrency(String str) {
        return EnumUtils.isValidEnumIgnoreCase(ProjectType.class, str) ? ProjectType.valueOf(str.toUpperCase()) : null;
    }
}
