package com.omar.models.faction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactionTest {
    Faction sampleFaction;
    public FactionTest(){
        this.sampleFaction = new Faction("Redosia", 15);
    }
    @Test
    void testInitArmies(){
        assertEquals("Redosia", sampleFaction.getName());
        assertEquals(15, sampleFaction.getArmy(0).getPosition());
        assertEquals(10, sampleFaction.getArmy(0).getFirepower());
    }
}