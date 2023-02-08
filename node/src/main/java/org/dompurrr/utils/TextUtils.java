package org.dompurrr.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class TextUtils {
    public boolean matchName(String name){
        return (name.length() >= 2 && name.length() <= 20) && (name.matches("[A-Za-zА-Яа-яЁё.]+"));
    }

    public boolean matchRoomName(String name){
        return (name.length() >= 2 && name.length() < 20) && (name.matches("[A-Za-zА-Яа-я0-9.]+"));
    }

    public boolean matchNum(String str){
        return (str.length() >= 1 && str.length() < 6) && (str.matches("[0-9]+"));
    }

    public String[] dataDestructor(String data){
        String[] tmp = data.split(" ");
        if (tmp.length == 2 && (matchToken(tmp[1])) && (matchNum(tmp[0]))){
            return tmp;
        }
        else{
            return null;
        }
    }

    private boolean matchToken(String token){
        return token.length() == 16 && token.matches("[A-Za-z0-9]+");
    }

    public String makeToken(){
        String upperLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String symbols = upperLetters + upperLetters.toLowerCase() + "0123456789";
        char[] buf = new char[16];
        for (int i=0; i<16; i++)
            buf[i] = symbols.charAt((new SecureRandom()).nextInt(symbols.length()));
        return new String(buf);
    }

    public String[] purchaseDestructor(String inp){
        String[] tmp = inp.split(" ");
        return tmp.length == 2 && (matchNum(tmp[1])) && (matchRoomName(tmp[0])) ? tmp : null;
    }

    public String[] addPurchaseDestructor(String inp){
        String[] tmp = inp.split(", ");
        if (tmp.length < 1 || tmp.length > 20)
            return null;
        for (String s : tmp)
            if (!matchNum(s))
                return null;
        return tmp;
    }
}
