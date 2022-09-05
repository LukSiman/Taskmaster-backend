package net.lukassimanavicius.TaskmasterBE.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TaskDTO {
    private String taskName;
    private Integer taskOrder;
    private String taskNote;
    private String taskStatus;
    private LocalTime taskStartTime;
    private LocalTime taskEndTime;
    private LocalDate taskDate;
    private String categoryName;
}
