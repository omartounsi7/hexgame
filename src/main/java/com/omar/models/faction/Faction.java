package com.omar.models.faction;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Faction {
    private final String name;
    private final List<Army> armies;
    public Faction(String name, int startingPos) {
        this.name = name;
        this.armies = new LinkedList<>();
        initArmies(startingPos);
    }
    public String getName() {
        return name;
    }
    public List<Army> getArmies(){
        return armies;
    }
    public Army getArmy(int index){
        return armies.get(index);
    }
    private void initArmies(int startingPos){
        armies.add(new Army(10, startingPos));
    }
    @Override
    public String toString() {
        return name + ": Armies -> " + armies;
    }
}
