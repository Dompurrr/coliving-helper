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
    INVITE_ROOM("/inviteToRoom");
    private final String cmd;

    ChatCommands(String cmd){
        this.cmd = cmd;
    }

    @Override
    public String toString(){
        return cmd;
    }

    public boolean equals(String cmd){
        return this.toString().equals(cmd);
    }
}
