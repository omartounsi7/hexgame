package com.omar.models.game;

import com.omar.models.faction.Faction;
import com.omar.models.world.World;

import java.util.Scanner;
import java.util.Set;

public class Game {
    private final World world;
    private final Faction [] factions;
    private final Scanner scanner;
    private GameStatus status;
    private Turn whosturn;
    public Game(Scanner scanner, World world, Faction [] factions) {
        this.scanner = scanner;
        this.world = world;
        this.factions = factions;
        this.status = GameStatus.ACTIVE;
        this.whosturn = Turn.P1TURN;
    }
    private void makeMove(){
        Faction currPlayer = factions[1];
        if(whosturn == Turn.P1TURN){
            currPlayer = factions[0];
            System.out.println("Player 1's turn.");
        } else if(whosturn == Turn.P2TURN){
            System.out.println("Player 2's turn.");
        }
        System.out.println(currPlayer);

        int armyChoice;
        while (true) {
            System.out.println("Enter an army's number to select it.");
            if (scanner.hasNextInt()) {
                armyChoice = scanner.nextInt();
                if (armyChoice >= 1 && armyChoice <= currPlayer.getArmies().size()) {
                    break;
                } else {
                    System.out.println("Invalid input!");
                }
            } else {
                scanner.next();
                System.out.println("Invalid input!");
            }
        }

        System.out.println("You have selected army #" + armyChoice + " :" + currPlayer.getArmy(armyChoice - 1));
        int armyPosition = currPlayer.getArmy(armyChoice - 1).getPosition();
        Set<Integer> possibleMoves = world.getTileNeighbors(armyPosition);
        System.out.println("Possible new positions are: " + possibleMoves);

        int posChoice;
        while (true) {
            System.out.println("Enter the new position of army #" + armyChoice);
            if (scanner.hasNextInt()) {
                posChoice = scanner.nextInt();
                if (possibleMoves.contains(posChoice)) {
                    break;
                } else {
                    System.out.println("Invalid input!");
                }
            } else {
                scanner.next();
                System.out.println("Invalid input!");
            }
        }

        currPlayer.getArmy(armyChoice - 1).setPosition(posChoice);
    }
    public void play(){
        while(status == GameStatus.ACTIVE){
            makeMove();
            if(whosturn == Turn.P1TURN){
                whosturn = Turn.P2TURN;
            } else if(whosturn == Turn.P2TURN){
                whosturn = Turn.P1TURN;
            }
        }
        if(status == GameStatus.P1WINS){
            System.out.println(factions[0].getName() + " has won!");
        } else if(status == GameStatus.P2WINS) {
            System.out.println(factions[1].getName() + " has won!");
        }
        System.out.println("Game over.");
        scanner.close();
    }
}
