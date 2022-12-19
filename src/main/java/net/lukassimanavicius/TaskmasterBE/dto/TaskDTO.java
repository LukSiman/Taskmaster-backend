package net.lukassimanavicius.TaskmasterBE.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class TaskDTO {
    private UUID taskUUID;
    private String taskName;
    private Integer taskOrder;
    private String taskNote;
    private Integer taskStatus;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private LocalTime taskStartTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private LocalTime taskEndTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate taskDate;
    private String categoryName;
}
