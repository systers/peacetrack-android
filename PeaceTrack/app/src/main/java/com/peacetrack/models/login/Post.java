package com.peacetrack.models.login;

/**
 * Created by bhagya on 6/28/15.
 */
public class Post {
    private int id;
    private String name;
    private int region;
    private int[] sectors;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int[] getSectors() {
        return sectors;
    }

    public void setSectors(int[] sectors) {
        this.sectors = sectors;
    }
}
