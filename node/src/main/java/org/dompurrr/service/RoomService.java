package org.dompurrr.service;

import org.dompurrr.entities.Resident;
import org.dompurrr.entities.Room;

public interface RoomService {
    /**
     * Attempts to create new room
     * @param resident user, attempting to create new room
     * @param roomName room name
     * @return info about new room or error message
     */
    String registerRoom(Resident resident, String roomName);

    /**
     * Returns info about room
     * @param room room to show info
     * @return room information
     */
    String getRoomInfo(Room room);

    /**
     * Adds user with specified name to resident's room
     * @param resident user, attempting to add user
     * @param residentName new resident name
     * @return success or error message
     */
    String newRoomResident(Resident resident, String residentName);

    /**
     * Remove user with userId from resident's room
     * @param resident resident, removing user from room
     * @param userId resident id to be removed
     * @return success or error message
     */
    String removeRoomResident(Resident resident, String userId);

    /**
     * Returns string with all residents from specified room
     * @param room to get residents
     * @return resident list-string
     */
    String residentList(Room room);

    String createRoomToken(Room room);

    boolean hasVacant(Room room);

    void dropVacant(Room room);

    boolean hasToken(Room room);
}
