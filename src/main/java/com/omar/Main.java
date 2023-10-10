package com.omar;

import com.omar.models.faction.Faction;
import com.omar.models.game.Game;
import com.omar.models.world.MapSize;
import com.omar.models.world.Tile;
import com.omar.models.world.World;
import com.omar.resources.FactionNames;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Game game = createGame();
        game.play();
    }
    private static Game createGame(){
        System.out.println("Welcome to Hex Wars!\n");
        Scanner scanner = new Scanner(System.in);
        char choice = mapSizeSelection(scanner);
        MapSize mapSize = getMapSize(choice);
        int size = MapSize.determineSize(mapSize);
        Tile[] tiles = new Tile[size * size];
        Map<Integer, Set<Integer>> adjacencyMatrix = new HashMap<>();
        World world = new World(tiles, adjacencyMatrix, size);
        Faction[] factions = new Faction[2];
        createFactions(factions, size, scanner);
        System.out.println("Let the game begin!");
        return new Game(scanner, world, factions);
    }
    private static char mapSizeSelection(Scanner scanner){
        System.out.println("The map size options are:");
        System.out.println("S: 4x4 grid");
        System.out.println("M: 6x6 grid");
        System.out.println("L: 8x8 grid");
        char choice = 'Z';
        while(choice != 'S' && choice != 'M' && choice != 'L'){
            System.out.println("Enter your desired map size:");
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
    private static String factionNameSelection(Scanner scanner, int playerNum){
        String factionName = "";
        while(factionName.isBlank()){
            System.out.println("Enter the name of player " + playerNum + "'s faction:");
            factionName = scanner.nextLine();
        }
        return factionName;
    }
    private static String getRandomFactionName(){
        Random random = new Random();
        int randomFactionIndex = random.nextInt(FactionNames.factionNames.length);
        return FactionNames.factionNames[randomFactionIndex];
    }
    private static void createFactions(Faction[] factions, int size, Scanner scanner){
        scanner.nextLine();
        String faction1Name = factionNameSelection(scanner, 1);
        String faction2Name = factionNameSelection(scanner, 2);
        factions[0] = new Faction(faction1Name, 0, new LinkedList<>());
        factions[1] = new Faction(faction2Name, size * size - 1, new LinkedList<>());
    }
}