package com.omar.model.faction;


public class Faction {
    private final String name;
    public Faction(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name;
    }
}
