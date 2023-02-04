package org.dompurrr.entities;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = "purchaseId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId;
    @Column(name = "purchase_name")
    private String dutyName;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident buyer;
    @Column(name = "sum")
    private Long sum;
}
