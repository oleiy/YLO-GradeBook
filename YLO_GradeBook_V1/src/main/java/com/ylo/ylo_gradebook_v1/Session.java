package com.ylo.ylo_gradebook_v1;

import models.Users;

//------------------------------------------------------------------------------------------------------
//--------------------------- This class stores the logged in user's data  -----------------------------
//------------------------------------------------------------------------------------------------------

public class Session {
    private static Users loggedUser;

    public static void setLoggedUser(Users user) {
        loggedUser = user;
    }

    public static Users getLoggedUser() {
        return loggedUser;
    }

    public static void clearSession() {
        loggedUser = null;
    }

    public static boolean isLoggedIn() {
        return loggedUser != null;
    }
}
