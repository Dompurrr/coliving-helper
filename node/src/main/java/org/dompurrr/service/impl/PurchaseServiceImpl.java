package org.dompurrr.service.impl;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.math.NumberUtils;
import org.dompurrr.dao.PurchaseDAO;
import org.dompurrr.dao.ResidentDAO;
import org.dompurrr.dao.RoomDAO;
import org.dompurrr.entities.Purchase;
import org.dompurrr.entities.Resident;
import org.dompurrr.entities.Room;
import org.dompurrr.entities.enums.UserState;
import org.dompurrr.service.PurchaseService;
import org.dompurrr.service.RoomService;
import org.dompurrr.utils.AnswerTemplates;
import org.dompurrr.utils.ErrorTemplates;
import org.dompurrr.utils.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j
public class PurchaseServiceImpl implements PurchaseService {

    private final TextUtils textUtils;
    private final PurchaseDAO purchaseDAO;
    private final ResidentDAO residentDAO;
    private final RoomDAO roomDAO;
    private final RoomService roomService;

    public PurchaseServiceImpl(TextUtils textUtils, PurchaseDAO purchaseDAO, ResidentDAO residentDAO, RoomDAO roomDAO, RoomService roomService) {
        this.textUtils = textUtils;
        this.purchaseDAO = purchaseDAO;
        this.residentDAO = residentDAO;
        this.roomDAO = roomDAO;
        this.roomService = roomService;
    }

    @Override
    public String createPurchase(Resident resident, String string) {
        String[] tmp = textUtils.purchaseDestructor(string);
        if (tmp != null){
            Room room = roomDAO.findByRoomId(resident.getRoom().getRoomId());
            Purchase newPurchase = new Purchase();
            newPurchase.setPurchaseName(tmp[0]);
            newPurchase.setSum(Long.parseLong(tmp[1]));
            newPurchase.setBuyer(resident);
            newPurchase.setRoom(room);
            purchaseDAO.save(newPurchase);
            resident.setUserState(UserState.PURCHASE_USER_ADD);
            residentDAO.save(resident);
            return ("Покупка создана. Перечислите тех кто скидывается на покупку.\n" +
                    "В формате набора чисел через запятую с пробелом.\n"+
                    roomService.residentList(room));
        }
        else{
            return ErrorTemplates.BAD_PURCHASE_NAME;
        }
    }

    @Override
    public String addUserPurchase(Resident resident, String string) {
        String[] tmp = textUtils.addPurchaseDestructor(string);
        if (tmp != null){
            ArrayList<Resident> resList = new ArrayList<>();
            for (String elem: tmp){
                Resident curRes = residentDAO.findByResidentId(Long.parseLong(elem));
                if (curRes != null && curRes.getRoom().getRoomId().equals(resident.getRoom().getRoomId()))
                    resList.add(curRes);
                else
                    return ErrorTemplates.BAD_PURCHASE_LIST_ID;
            }
            // Hope it takes last purchase
            Purchase purchase = purchaseDAO.findByRoom(resident.getRoom()).get(0);
            purchase.getResidents().addAll(resList);
            purchaseDAO.save(purchase);
            resident.setUserState(UserState.WAIT_FOR_COMMAND);
            residentDAO.save(resident);
            return AnswerTemplates.PURCHASE_LIST_SUCCESS;
        }
        else{
            return ErrorTemplates.BAD_PURCHASE_LIST;
        }
    }

    @Override
    public String getPurchases(Room room) {
        if (room == null){
            log.error("Provided null room");
            return ErrorTemplates.NO_ROOM;
        }
        else{
            Room updatedRoom = roomDAO.findRoomWithPurchasesByRoomId(room.getRoomId());
            StringBuilder res = new StringBuilder();
            for (Purchase curPurchase: updatedRoom.getPurchases()){
                Purchase updatedPurchase = purchaseDAO.findPurchaseWithBuyerByPurchaseId(curPurchase.getPurchaseId());
                res.append(updatedPurchase.getPurchaseId()).append(")").append(updatedPurchase.getPurchaseName()).append("\n").append("   на сумму: ").append(updatedPurchase.getSum()).append("\n").append("   покупатель: ").append(updatedPurchase.getBuyer().getResidentName()).append("\n").append("   скидывались: ").append(getPurchaseResidents(updatedPurchase)).append("\n");
            }
            return res.toString();
        }
    }

    @Override
    public String getPurchaseResidents(Purchase purchase) {
        Purchase updatedPurchase = purchaseDAO.findByPurchaseId(purchase.getPurchaseId());
        List<Resident> residentList = updatedPurchase.getResidents();
        StringBuilder res = new StringBuilder();
        for (Resident curRes: residentList){
            res.append(curRes.getResidentName()).append(" ");
        }
        return res.toString();
    }

    @Override
    @Transactional
    public String deletePurchase(Resident resident, String textId) {
        if (NumberUtils.isNumber(textId)){
            Long purchaseId = Long.parseLong(textId);
            Room room = roomDAO.findByRoomId(resident.getRoom().getRoomId());
            if (room != null){
                Purchase purchase = purchaseDAO.findByPurchaseId(purchaseId);
                if (purchase != null){
                    if (purchase.getBuyer().getResidentId().equals(resident.getResidentId())) {
                        purchase.setRoom(null);
                        purchase.setBuyer(null);
                        purchase.setResidents(null);
                        purchaseDAO.save(purchase);
                        resident.setUserState(UserState.WAIT_FOR_COMMAND);
                        residentDAO.save(resident);
                        return "Покупка " + purchaseId + " удалена.";
                    }
                    else return ErrorTemplates.BAD_PURCHASE_ID;
                }
                else return ErrorTemplates.BAD_PURCHASE_ID;
            }
            else return ErrorTemplates.NO_ROOM;
        }
        else return ErrorTemplates.BAD_NUMBER;
    }
}
