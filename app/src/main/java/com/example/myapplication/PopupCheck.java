package com.example.myapplication;

public class PopupCheck {
    static boolean popupCheck = false;


    public static boolean isPopupCheck() {
        return popupCheck;
    }

    public static void setPopupCheck(boolean popupCheck) {
        PopupCheck.popupCheck = popupCheck;
    }
}
