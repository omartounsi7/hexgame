package com.omar.models.faction;

public class Faction {
    private final String name;
    public Faction(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "{" + name + "}";
    }
    public String getName() {
        return name;
    }
}
