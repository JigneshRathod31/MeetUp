package com.jignesh.meetup.models;

import java.util.List;

public class PeopleModel {
    public int profileImg;
    public String uid;
    public String name;
    public String email;
    public String mobile;
    public boolean gender;
    public String dob;
    public String state;
    public List<String> languages;
    public List<String> sports;
    public List<String> eSports;
    public String profession;
    public List<String> hobbies;

    public PeopleModel(int profileImg, String uid, String name, String email, String mobile, boolean gender, String dob, String state, List<String> languages, List<String> sports, List<String> eSports, String profession, List<String> hobbies) {
        this.profileImg = profileImg;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.dob = dob;
        this.state = state;
        this.languages = languages;
        this.sports = sports;
        this.eSports = eSports;
        this.profession = profession;
        this.hobbies = hobbies;
    }
}
