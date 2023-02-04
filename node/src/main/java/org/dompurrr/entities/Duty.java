package org.dompurrr.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "duty_id")
    private Long dutyId;
    @Column(name = "duty_name")
    private String dutyName;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident worker;
    @Column(name = "notification_date")
    private LocalDateTime notificationDate;
    @Column(name = "deadline_date")
    private LocalDateTime deadlineDate;
    @Column(name = "completed")
    private Boolean completed;
}
