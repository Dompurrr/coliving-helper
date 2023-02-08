package org.dompurrr.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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
    private Long purchaseId;
    @Column(name = "purchase_name")
    private String purchaseName;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Resident buyer;
    @Column(name = "sum")
    private Long sum;
    @ManyToMany
    @JoinTable(name = "purchase_resident",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "resident_id"))
    List<Resident> residents;
}
