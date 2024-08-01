package com.jignesh.meetup.utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;

public class Constants {

    public static final FirebaseUser CURRENT_USER = FirebaseAuth.getInstance().getCurrentUser();

    public static final String USER_ID = CURRENT_USER.getUid();

    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
}
