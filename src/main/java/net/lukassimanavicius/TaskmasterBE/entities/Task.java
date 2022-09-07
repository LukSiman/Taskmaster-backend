package net.lukassimanavicius.TaskmasterBE.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "task")
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
    private String taskName;

    @Column(name = "task_order")
    private Integer taskOrder;

    @Column(name = "task_note")
    private String taskNote;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "task_start_time")
    private LocalTime taskStartTime;

    @Column(name = "task_end_time")
    private LocalTime taskEndTime;

    @Column(name = "task_date")
    private LocalDate taskDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
