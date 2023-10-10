package com.omar.models.game;

import com.omar.models.faction.Faction;
import com.omar.models.world.World;
import java.util.Scanner;

public class Game {
    private final World world;
    private final Faction [] factions;
    private final Scanner scanner;
    private GameStatus status;
    public Game(Scanner scanner, World world, Faction [] factions) {
        this.scanner = scanner;
        this.world = world;
        this.factions = factions;
        this.status = GameStatus.ACTIVE;
    }
    public void play(){
        while(status == GameStatus.ACTIVE){
            status = GameStatus.P2WINS;
        }
        if(status == GameStatus.P1WINS){
            System.out.println(factions[0].getName() + " has won!");
        } else {
            System.out.println(factions[1].getName() + " has won!");
        }
        System.out.println("Game over.");
        scanner.close();
    }
    public World getWorld() {
        return world;
    }
    public Faction getFaction(int index) {
        return factions[index];
    }
}
