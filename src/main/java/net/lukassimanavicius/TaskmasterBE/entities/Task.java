package net.lukassimanavicius.TaskmasterBE.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
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
    @NotNull(message = "Invalid order!")
    @Min(value = 1, message = "Invalid order!")
    private Integer taskOrder;

    @Column(name = "task_note")
    private String taskNote;

//    @Min(value = 0, message = "Status cannot be lower than 0")
//    @Max(value = 1, message = "Status cannot be higher than 1")
//    @NotNull
    @Column(name = "task_status")
    private Integer taskStatus;

//    @Temporal(TemporalType.TIME)
    @Column(name = "task_start_time")
    private LocalTime taskStartTime;

//    @Temporal(TemporalType.TIME)
    @Column(name = "task_end_time")
    private LocalTime taskEndTime;

//    @DateTimeFormat(pattern = "yyyy/mm/dd")
//    @Temporal(TemporalType.DATE)
    @Column(name = "task_date")
    private LocalDate taskDate;

    @ManyToOne
//    @NotNull
    @JoinColumn(name = "category_id")
    private Category category;
}
