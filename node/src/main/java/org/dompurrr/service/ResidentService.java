package org.dompurrr.service;

import org.dompurrr.entities.Resident;
import org.telegram.telegrambots.meta.api.objects.User;

public interface ResidentService {
    /**
     * Gets existed User or creates new one
     * @param sender message sender
     * @return Resident entity of sender
     */
    Resident registerOrGetAccount(User sender);
    /**
     * Creates new record in table for specified sender
     * @param sender message sender
     * @return Resident entity of sender
     */
    Resident registerNew(User sender);
    /**
     * Returns if specified Resident has token
     * @param resident resident to check token
     * @return
     */
    boolean hasToken(Resident resident);
    /**
     * Returns if specified Resident has room
     * @param resident Resident to check room existence
     * @return
     */
    boolean hasRoom(Resident resident);
    /**
     * Changes provided resident name to newName
     * @param resident resident to change name
     * @param newName new name
     * @return success message or error description
     */
    String updateName(Resident resident, String newName);

    /**
     * Gets info about provided resident
     * @param resident to get info about
     * @return information about resident
     */
    String residentInfo(Resident resident);

    /**
     * Generates token for specified resident
     * @param resident to create token
     * @return created token or existed token
     */
    String generateToken(Resident resident);

    /**
     * Attempts to add specified user to room with specified data
     * @param resident
     * @param data
     * @return result
     */
    String joinRoom(Resident resident, String data);
}
