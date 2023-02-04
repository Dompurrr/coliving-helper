package org.dompurrr.entities;

import lombok.*;
import org.dompurrr.entities.enums.AccountStatus;
import org.dompurrr.entities.enums.UserState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = "residentId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resident")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resident_id")
    private Long residentId;
    @Column(name = "resident_name")
    private String residentName;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @Column(name = "vk_page_ref")
    private String vkPageRef;
    @Column(name = "tg_page_ref")
    private Long tgPageRef;
    @Column(name = "ref_token")
    private String refToken;
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Duty> duties = new ArrayList<>();
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Purchase> purchases = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private UserState userState;
}
