package org.dompurrr.dao;

import org.dompurrr.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDAO extends JpaRepository<Room, Long> {
    Room findByRoomId(Long id);
    Room findByTgChatRef(String tgChat);
}
