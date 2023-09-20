package com.omar.models.faction;

import java.util.HashMap;
import java.util.Map;

public class Faction {
    private final String name;
    private final Map<Army, Integer> armies;
    public Faction(String name, int startingPos) {
        this.name = name;
        this.armies = new HashMap<>();
        initArmies(startingPos);
    }
    public String getName() {
        return name;
    }
    public void initArmies(int startingPos){
        armies.put(new Army(10), startingPos);
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append(name).append("}\n");
        for (Army v : armies.keySet()) {
            builder.append(v.toString()).append(": ");
            builder.append(armies.get(v).toString()).append(" ");
            builder.append("\n");
        }
        return (builder.toString());
    }
}
