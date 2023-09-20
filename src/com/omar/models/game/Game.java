package com.omar.models.game;

import com.omar.models.map.MapSize;
import com.omar.models.map.World;

import java.util.Scanner;

public class Game {
    World world;
    public Game() {
        System.out.println("Welcome to Hex Wars!\n");
        char choice = mapSizeSelection();
        MapSize mapSize = getMapSize(choice);
        this.world = new World(mapSize);
    }
    public char mapSizeSelection(){
        System.out.println("The map size options are:");
        System.out.println("S: 4x4 grid");
        System.out.println("M: 6x6 grid");
        System.out.println("L: 8x8 grid");
        Scanner scanner = new Scanner(System.in);
        char choice = 'Z';
        while(choice != 'S' && choice != 'M' && choice != 'L'){
            System.out.println("Please enter your desired map size:");
            choice = scanner.next().charAt(0);
        }
        scanner.close();
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
}
