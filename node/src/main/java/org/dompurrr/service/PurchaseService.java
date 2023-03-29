package org.dompurrr.service;

import org.dompurrr.entities.Purchase;
import org.dompurrr.entities.Resident;
import org.dompurrr.entities.Room;

public interface PurchaseService {

    String createPurchase(Resident resident, String string);

    String addUserPurchase(Resident resident, String string);

    String getPurchases(Room room);

    String getPurchaseResidents(Purchase purchase);

    String deletePurchase(Resident resident, String purchaseId);
}
