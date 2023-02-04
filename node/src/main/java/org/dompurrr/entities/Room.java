package org.dompurrr.entities;

import lombok.*;
import org.dompurrr.entities.enums.RoomStatus;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = "roomId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "room_name")
    private String roomName;
    @Column(name = "vk_chat_ref")
    private String vkChatRef;
    @Column(name = "tg_chat_ref")
    private String tgChatRef;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Resident> residentList = new ArrayList<>();
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Duty> duties = new ArrayList<>();
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Purchase> purchases = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    @Column(name = "room_token")
    private String token;
}
