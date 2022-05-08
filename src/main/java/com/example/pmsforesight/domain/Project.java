package com.example.pmsforesight.domain;

import com.example.pmsforesight.enums.ProjectType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Project implements Serializable {
    @Id
    private String uid;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProjectType type;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String parentUid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Project project = (Project) o;
        return uid != null && Objects.equals(uid, project.uid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
