package com.mobiletrain.www.activitybeen;

/**
 * Created by aaa on 15-4-21.
 */
public class FragmentUser {
    private String id;
    private String name;
    private String gender;
    private String bio;
    private String avatar;

    public FragmentUser(String id, String name, String gender, String bio, String avatar) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.bio = bio;
        this.avatar = avatar;
    }

    public FragmentUser( ) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
