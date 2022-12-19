package net.lukassimanavicius.TaskmasterBE.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "task")
@DynamicInsert
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskID;

    @Type(type = "uuid-char")
    @Column(name = "task_uuid")
    private UUID taskUUID;

    @Column(name = "task_name")
    @NotNull(message = "Name must be between 2 and 50 characters!")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters!")
    private String taskName;

    @Column(name = "task_order")
    @NotNull(message = "Invalid order value!")
    @Min(value = 1, message = "Invalid order value!")
    private Integer taskOrder;

    @Column(name = "task_note")
    @Size(max = 200, message = "Note cannot be longer than 200 characters!")
    private String taskNote;

    @Column(name = "task_status")
    @Min(value = 0, message = "Status has to be 0 or 1!")
    @Max(value = 1, message = "Status has to be 0 or 1!")
    @NotNull(message = "Status cannot be null!")
    private Integer taskStatus;

    @Column(name = "task_start_time")
    private LocalDateTime taskStartTime;

    @Column(name = "task_end_time")
    private LocalDateTime taskEndTime;

//    @Column(name = "task_date")
//    private LocalDate taskDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
