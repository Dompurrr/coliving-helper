package org.dompurrr.service.enums;

public enum ChatCommands {
    START("/start"),
    HELP("/help"),
    REGISTRATION("/register"),
    INFO("/info"),
    CHANGE_NAME("/changeName"),
    CANCEL("/cancel"),
    ROOM("/room"),
    ADD_ROOM("/addUser"),
    REMOVE_ROOM("/removeUser"),
    DELETE_ROOM("/deleteRoom"),
    JOIN_ROOM("/joinRoom"),
    INVITE_ROOM("/inviteToRoom"),
    PURCHASE_CREATE("/createPurchase"),
    PURCHASE_GET("/getPurchases"),
    PURCHASE_DELETE("/removePurchase"),
    PURCHASE_PAY("/payPurchase");
    private final String cmd;

    ChatCommands(String cmd){
        this.cmd = cmd;
    }

    @Override
    public String toString(){
        return cmd;
    }

    public boolean cmdEquals(String cmd){
        return this.toString().equals(cmd);
    }
}
