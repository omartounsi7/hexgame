package com.omar.model;

import java.util.LinkedList;
import java.util.List;

public class AI {
    public static Board minimax(Board currState, int depth, int faction){
        if(depth == 0 || currState.checkVictory() != 0){
            currState.evaluate();
            System.out.print("Score is: ");
            System.out.println(currState.getScore());
            return currState;
        }
        Board bestState = currState;

        if(faction == 1){ // MAX PLAYER
            double maxEval = Double.NEGATIVE_INFINITY;
            List<Board> possStates = getPossibleStates(currState, faction);
            for(Board possState : possStates){
                double eval = minimax(possState, depth - 1, 2).getScore();
                maxEval = Math.max(maxEval, eval);
                if (maxEval == eval){
                    bestState = possState;
                }
            }

        } else { // MIN PLAYER
            double minEval = Double.POSITIVE_INFINITY;
            List<Board> possStates = getPossibleStates(currState, faction);
            for(Board possState : possStates){
                double eval = minimax(possState, depth - 1, 1).getScore();
                minEval = Math.min(minEval, eval);
                if (minEval == eval){
                    bestState = possState;
                }
            }
        }

        return bestState;
    }
    public static List<Board> getPossibleStates(Board currState, int faction){
        List<Board> possStates = new LinkedList<>();

        List<int[]> armyPos = currState.getArmiesPos(faction);

        for(int[] position : armyPos){
            int startX = position[0];
            int startY = position[1];
            List<int[]> adjacentCoords = currState.getAdjacent(startX, startY);
            for(int[] coords : adjacentCoords){
                int endX = coords[0];
                int endY = coords[1];

                Board newState = currState.clone();

                Tile startTile = newState.getTiles()[startX][startY];
                Tile endTile = newState.getTiles()[endX][endY];

                if(faction == 1){
                    newState.moveArmy(startX, startY, endX, endY, TurnStatus.P1TURN);
                } else {
                    newState.moveArmy(startX, startY, endX, endY, TurnStatus.P2TURN);
                }


                possStates.add(newState);
            }
        }
        return possStates;
    }
}
