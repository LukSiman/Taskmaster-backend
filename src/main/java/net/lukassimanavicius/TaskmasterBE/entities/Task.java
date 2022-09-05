package net.lukassimanavicius.TaskmasterBE.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "task")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskID;

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
}
