package ca.cmpt213.as4.Model;

import ca.cmpt213.as4.Controllers.MazeController;
import ca.cmpt213.as4.restapi.ApiBoardWrapper;
import ca.cmpt213.as4.restapi.ApiGameWrapper;
import ca.cmpt213.as4.restapi.ApiLocationWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * class for handling the general game play.
 * Cheese is randomly generated and cats are randomly moved.
 * Mouse moves depends on the user input
 * Handles cheat code: show the entire map / change the number of cheese to 1
 * Checks if the mouse have collected the cheese (win the game) or is eaten by the cat (lose the game)
 */

public class GameManager {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;

    private Mouse mouse;
    private Cheese cheese;
    private ArrayList<Cat> catList = new ArrayList<>();
    private Maze maze = new Maze();

    private static int numCheeseCollected = 0;
    private static int numCheese = 5;
    private boolean gotCheese = false;
    private static boolean deadMouse = false;
    private static boolean winGame = false;
    private String[] previousMove = {"", "", ""};

    public GameManager(){
        makeMouse();
        makeCat();
        maze.mazeMaker();
    }

    public void makeMouse() {
        mouse = new Mouse (1,1);
    }

    public void makeCat(){
        catList.add(new Cat(1, 18));
        catList.add(new Cat(13, 1));
        catList.add(new Cat(13, 18));
    }

    public void makeCheese(ApiBoardWrapper board){
        Random random = new Random();
        int randomHeight = random.nextInt(HEIGHT - 3) + 2;
        int randomWidth = random.nextInt(WIDTH - 3) + 2;

        if (board.hasWalls[randomHeight][randomWidth]){
            makeCheese(board);
        }
        else{
            board.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(randomHeight, randomWidth);
        }
    }

    public static int getNumCheeseCollected() {
        return numCheeseCollected;
    }

    public static int getNumCheese() {
        return numCheese;
    }

    public void showMap(ApiBoardWrapper maze){
        for (int i = 0; i < maze.boardHeight; i++) {
               for (int j = 0; j < maze.boardWidth; j++) {
                   maze.isVisible[i][j] = true;
               }
        }
    }

    public void eatCheese(ApiGameWrapper game, ApiBoardWrapper maze){
        if (maze.cheeseLocation.x == maze.mouseLocation.x && maze.cheeseLocation.y == maze.mouseLocation.y) {
            game.numCheeseFound++;
            //numCheeseCollected++;
            game.eatenCheese = true;
            makeCheese(maze);
            if (game.numCheeseFound == game.numCheeseGoal) {
                showMap(maze);
                game.isGameWon = true;
            }
        }
    }

    public void mouseMoveToCat (int height, int width, ApiBoardWrapper maze, ApiGameWrapper wrapper){
        for (int i = 0; i < maze.catLocations.size(); i++) {
            if (maze.catLocations.get(i).x == width && maze.catLocations.get(i).y == height) {
                showMap(maze);
                wrapper.isGameLost = true;
            }
        }
    }

    public boolean moveMouse(String direction, ApiBoardWrapper maze, ApiGameWrapper wrapper){
        boolean success = true;
        switch (direction) {
            case "up":
                //move up
                if (maze.hasWalls[maze.mouseLocation.y - 1][maze.mouseLocation.x]) { // if wall
                    success = false; // fail
                } else {
                    revealMouse(maze.mouseLocation.y - 1, maze.mouseLocation.x, maze);
                    mouseMoveToCat(maze.mouseLocation.y - 1, maze.mouseLocation.x, maze, wrapper);
                    success = true; // success
                }
                break;
            case "down":
                // move down
                if (maze.hasWalls[maze.mouseLocation.y + 1][maze.mouseLocation.x]) { // if wall
                    success = false;
                } else {
                    revealMouse(maze.mouseLocation.y + 1, maze.mouseLocation.x, maze);
                    mouseMoveToCat(maze.mouseLocation.y + 1, maze.mouseLocation.x, maze, wrapper);
                    success = true;
                }
                break;
            case "left":
                //move left
                if (maze.hasWalls[maze.mouseLocation.y][maze.mouseLocation.x - 1]) { // if wall
                    success = false;
                } else {
                    revealMouse(maze.mouseLocation.y, maze.mouseLocation.x - 1, maze);
                    mouseMoveToCat(maze.mouseLocation.y, maze.mouseLocation.x - 1, maze, wrapper);
                    success = true;
                }
                break;
            case "right":
                //move right
                if (maze.hasWalls[maze.mouseLocation.y][maze.mouseLocation.x + 1]) { // if wall
                    success = false;
                } else {
                    revealMouse(maze.mouseLocation.y, maze.mouseLocation.x + 1, maze);
                    mouseMoveToCat(maze.mouseLocation.y, maze.mouseLocation.x + 1, maze, wrapper);
                    success = true;
                }
                break;
        }
        return success;
    }

    public void revealMouse(int mouseHeight, int mouseWidth, ApiBoardWrapper board){
        int mouseRight = 1;
        int mouseDown = 1;
        for (int mouseLeft = -1; mouseLeft <= mouseRight; mouseLeft++){
            for (int mouseUp = -1; mouseUp <= mouseDown; mouseUp++){
                if (maze.getMaze()[mouseHeight + mouseUp][mouseWidth + mouseLeft].equals("wall")) {
                    maze.setVisited(mouseHeight + mouseUp, mouseWidth + mouseLeft);
                } else if (maze.getMaze()[mouseHeight + mouseUp][mouseWidth + mouseLeft].equals("empty")){
                    maze.setVisited(mouseHeight + mouseUp, mouseWidth + mouseLeft);
                }

                board.isVisible[mouseHeight + mouseUp][mouseWidth + mouseLeft] = true;
            }
        }
        maze.setVisited(mouseHeight, mouseWidth);
    }

    public void catMoveToMouse (int index, int height, int width, ApiBoardWrapper board, ApiGameWrapper wrapper){
        if (board.mouseLocation.x == width && board.mouseLocation.y == height) {
            showMap(board);
            wrapper.isGameLost = true;
        }
    }

    public void moveCat(ApiBoardWrapper board, ApiGameWrapper wrapper) {
        RandomDirection direction = new RandomDirection();
        direction.shuffleDirection();
        List<String> randomDirection = direction.getRandomDirection();

        for (int i = 0; i < board.catLocations.size(); i++) {
            int catHeight = board.catLocations.get(i).y;
            int catWidth = board.catLocations.get(i).x;
            innerloop:
            for (int j = 0; j < randomDirection.size(); j++) {
                switch (randomDirection.get(j)) {
                    case "up":
                        if (previousMove[i].equals("down")) {
                            if (!(board.hasWalls[catHeight][catWidth - 1]
                                && board.hasWalls[catHeight][catWidth + 1]
                                    && board.hasWalls[catHeight + 1][catWidth])) {
                                break;
                            }
                        }

                        if (!(board.hasWalls[catHeight - 1][catWidth])) {
                            board.catLocations.get(i).y--;
                            catMoveToMouse(i, catHeight - 1, catWidth, board, wrapper);
                            previousMove[i] = "up";
                            break innerloop;
                        }
                        break;
                    case "down":
                        if (previousMove[i].equals("up")) {
                            if (!(board.hasWalls[catHeight][catWidth - 1]
                                    && board.hasWalls[catHeight][catWidth + 1]
                                    && board.hasWalls[catHeight - 1][catWidth])) {
                                break;
                            }
                        }

                        if (!(board.hasWalls[catHeight + 1][catWidth])) {
                            board.catLocations.get(i).y++;
                            catMoveToMouse(i, catHeight + 1, catWidth, board, wrapper);
                            previousMove[i] = "down";
                            break innerloop;
                        }
                        break;
                    case "left":
                        if (previousMove[i].equals("right")) {
                            if (!(board.hasWalls[catHeight - 1][catWidth]
                                    && board.hasWalls[catHeight + 1][catWidth]
                                    && board.hasWalls[catHeight][catWidth + 1])) {
                                break;
                            }
                        }

                        if (!(board.hasWalls[catHeight][catWidth - 1])) {
                            board.catLocations.get(i).x--;
                            previousMove[i] = "left";
                            catMoveToMouse(i, catHeight, catWidth - 1, board, wrapper);
                            break innerloop;
                        }
                        break;
                    case "right":
                        if (previousMove[i].equals("left")) {
                            if (!(board.hasWalls[catHeight + 1][catWidth]
                                    && board.hasWalls[catHeight - 1][catWidth]
                                    && board.hasWalls[catHeight][catWidth - 1])) {
                                break;
                            }
                        }

                        if (!(board.hasWalls[catHeight][catWidth + 1])) {
                            board.catLocations.get(i).x++;
                            previousMove[i] = "right";
                            catMoveToMouse(i, catHeight, catWidth + 1, board, wrapper);
                            break innerloop;
                        }
                        break;
                }
            }
        }
    }
}
