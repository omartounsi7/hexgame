package com.omar.models.game;

import com.omar.models.faction.Faction;
import com.omar.models.world.MapSize;
import com.omar.models.world.World;
import com.omar.resources.FactionNames;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private final World world;
    private final Faction [] factions;
    private final Scanner scanner;
    public Game() {
        System.out.println("Welcome to Hex Wars!\n");
        this.scanner = new Scanner(System.in);
        char choice = mapSizeSelection();
        MapSize mapSize = getMapSize(choice);
        this.world = new World(mapSize);
        this.factions = new Faction[2];
        String userFactionName = factionNameSelection();
        String enemyFactionName = getRandomFactionName();
        int size = MapSize.determineSize(mapSize);
        createFactions(userFactionName, enemyFactionName, size);
        System.out.println("You shall face the " + enemyFactionName + "!");
        getScanner().close();
    }
    public World getWorld() {
        return world;
    }
    public Faction getFaction(int index) {
        return factions[index];
    }
    public Scanner getScanner() {
        return scanner;
    }
    public char mapSizeSelection(){
        System.out.println("The map size options are:");
        System.out.println("S: 4x4 grid");
        System.out.println("M: 6x6 grid");
        System.out.println("L: 8x8 grid");
        char choice = 'Z';
        while(choice != 'S' && choice != 'M' && choice != 'L'){
            System.out.println("Enter your desired map size.");
            choice = getScanner().next().charAt(0);
        }
        return choice;
    }
    public MapSize getMapSize(char choice){
        return switch (choice) {
            case 'S' -> MapSize.SMALL;
            case 'M' -> MapSize.MEDIUM;
            case 'L' -> MapSize.LARGE;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }
    public String factionNameSelection(){
        getScanner().nextLine();
        String factionName = "";
        while(factionName.isBlank()){
            System.out.println("Enter the name of your glorious faction!");
            factionName = getScanner().nextLine();
        }
        return factionName;
    }
    public void createFactions(String userFactionName, String enemyFactionName, int size){
        factions[0] = new Faction(userFactionName, 0);
        factions[1] = new Faction(enemyFactionName, size * size - 1);
    }
    public String getRandomFactionName(){
        Random random = new Random();
        int randomFactionIndex = random.nextInt(FactionNames.factionNames.length);
        return FactionNames.factionNames[randomFactionIndex];
    }
}
