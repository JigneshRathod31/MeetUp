package com.jignesh.meetup.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferenceData {

    public static String state, profession;
    public static Set<String> languages, sports, eSports, hobbies;
    public static Set<String> chatUserIds = new HashSet<>();

    public static void storeLocationLanguages(Context context, String state, Set<String> languages){
        SharedPreferences sp = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("state", state);
        editor.putStringSet("languages", languages);
        editor.apply();

        SharedPreferenceData.state = state;
        SharedPreferenceData.languages = languages;
    }

    public static void storeSports(Context context, Set<String> sports, Set<String> eSports){
        SharedPreferences sp = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("sports", sports);
        editor.putStringSet("eSports", eSports);
        editor.apply();

        SharedPreferenceData.sports = sports;
        SharedPreferenceData.eSports = eSports;
    }

    public static void storeProfessionHobby(Context context, String profession, Set<String> hobbies){
        SharedPreferences sp = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("profession", profession);
        editor.putStringSet("hobbies", hobbies);
        editor.apply();

        SharedPreferenceData.profession = profession;
        SharedPreferenceData.hobbies = hobbies;
    }

    public static void addChatUserIds(Context context, String id){
        chatUserIds.add(id);

        SharedPreferences sp = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("chatUserIds", chatUserIds);
        editor.apply();
    }

    public static void storeUserDetails(Context context, String name, String email, String mobile, boolean gender, String dob){
        SharedPreferences sp = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.putBoolean("gender", gender);
        editor.putString("dob", dob);
        editor.apply();
    }
}
