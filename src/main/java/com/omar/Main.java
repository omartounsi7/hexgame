package com.omar;

import com.omar.models.faction.Faction;
import com.omar.models.game.Game;
import com.omar.models.world.MapSize;
import com.omar.models.world.World;
import com.omar.resources.FactionNames;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Hex Wars!\n");
        Scanner scanner = new Scanner(System.in);
        char choice = mapSizeSelection(scanner);
        MapSize mapSize = getMapSize(choice);
        World world = new World(mapSize);
        Faction[] factions = new Faction[2];

        String userFactionName = factionNameSelection(scanner);
        String enemyFactionName = getRandomFactionName();
        int size = MapSize.determineSize(mapSize);
        createFactions(factions, userFactionName, enemyFactionName, size);

        System.out.println("You shall face the " + enemyFactionName + "!");
        Game game = new Game(scanner, world, factions);
        scanner.close();
    }
    private static char mapSizeSelection(Scanner scanner){
        System.out.println("The map size options are:");
        System.out.println("S: 4x4 grid");
        System.out.println("M: 6x6 grid");
        System.out.println("L: 8x8 grid");
        char choice = 'Z';
        while(choice != 'S' && choice != 'M' && choice != 'L'){
            System.out.println("Enter your desired map size.");
            choice = scanner.next().charAt(0);
        }
        return choice;
    }
    private static MapSize getMapSize(char choice){
        return switch (choice) {
            case 'S' -> MapSize.SMALL;
            case 'M' -> MapSize.MEDIUM;
            case 'L' -> MapSize.LARGE;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }
    private static String factionNameSelection(Scanner scanner){
        scanner.nextLine();
        String factionName = "";
        while(factionName.isBlank()){
            System.out.println("Enter the name of your glorious faction!");
            factionName = scanner.nextLine();
        }
        return factionName;
    }
    private static String getRandomFactionName(){
        Random random = new Random();
        int randomFactionIndex = random.nextInt(FactionNames.factionNames.length);
        return FactionNames.factionNames[randomFactionIndex];
    }
    private static void createFactions(Faction[] factions, String userFactionName, String enemyFactionName, int size){
        factions[0] = new Faction(userFactionName, 0);
        factions[1] = new Faction(enemyFactionName, size * size - 1);
    }
}