package org.dompurrr.service;

import org.dompurrr.entities.Resident;

public interface MainService {
    /**
     * Sorts message depending on user's command input
     * @param resident user, inputted command
     * @param command user's input
     * @return result message
     */
    String proceedCommand(Resident resident, String command);

    /**
     * Cancel multistep operation
     * @param resident user, wanted to stop operation
     * @return result
     */
    String stopOperation(Resident resident);

    /**
     * Starts multistep room operation, or returns room info
     * @param resident
     * @return room info
     */
    String roomOperation(Resident resident);

    /**
     * Starts multistep user add operation
     * @param resident
     * @return result
     */
    String roomAddOperation(Resident resident);

    /**
     * Starts multistep name change operation
     * @param resident
     * @return result
     */
    String nameChangeOperation(Resident resident);

    /**
     * Starts multistep user remove operation
     * @param resident
     * @return result
     */
    String roomRemoveOperation(Resident resident);

    /**
     * Creates or returns room join info
     * @param resident
     * @return result
     */
    String roomInviteOperation(Resident resident);

    /**
     * Start multistep join operation
     * @param resident
     * @return result
     */
    String roomJoinOperation(Resident resident);

    /**
     * Start multistep purchase creation
     * @param resident
     * @return result
     */
    String createPurchaseOperation(Resident resident);
}
