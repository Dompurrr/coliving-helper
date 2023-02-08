package org.dompurrr.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = "dutyId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "duty")
public class Duty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dutyId;
    @Column(name = "duty_name")
    private String dutyName;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToMany
    @JoinTable(name = "duty_worker",
            joinColumns = @JoinColumn(name = "duty_id"),
            inverseJoinColumns = @JoinColumn(name = "worker_id"))
    private List<Resident> workers;
    @Column(name = "notification_date")
    private LocalDateTime notificationDate;
    @Column(name = "deadline_date")
    private LocalDateTime deadlineDate;
    @Column(name = "completed")
    private Boolean completed;
}
