package org.dompurrr.dao;

import org.dompurrr.entities.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDAO extends JpaRepository<Room, Long> {

    Room findByRoomId(Long id);

    @EntityGraph(attributePaths = {"residentList"})
    Room findRoomWithResidentsByRoomId(Long id);

    @EntityGraph(attributePaths = {"purchases"})
    Room findRoomWithPurchasesByRoomId(Long roomId);

    Room findByTgChatRef(String tgChat);
}
