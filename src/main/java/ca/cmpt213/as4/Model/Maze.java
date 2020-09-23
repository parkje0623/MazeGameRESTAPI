package ca.cmpt213.as4.Model;

import ca.cmpt213.as4.restapi.ApiBoardWrapper;

import java.util.List;
import java.util.Random;

/**
 * class for initializing the maze, using DFS recursion method, also contains cycles.
 * All maze cells except outer walls are initialized to non-visited.
 */

public class Maze {
    final private static int HEIGHT = 15;
    final private static int WIDTH = 20;

    private static String[][] maze = new String[HEIGHT][WIDTH];
    private static Boolean[][] visited = new Boolean[HEIGHT][WIDTH];
    private static Random random = new Random();

    public Maze() {
        this.maze = maze;
        this.visited = visited;
    }

    public int getHeight() {
        return HEIGHT;
    }
    public int getWidth() {
        return WIDTH;
    }

    public static String[][] getMaze() {
        return maze;
    }
    public static String getMazeInfo(int height, int width) {
        return getMaze()[height][width];
    }
    public void setMaze(int heightPosition, int widthPosition, String value) {
        maze[heightPosition][widthPosition] = value;
    }

    public Boolean[][] getVisited() {
        return visited;
    }

    public static void setVisited(int height, int width) {
        visited[height][width] = true;
    }

    public static String[][] mazeMaker(){
        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){
                maze[i][j] = "wall";
                visited[i][j] = false;
            }
        }
        int randomHeight = random.nextInt(HEIGHT);
        while (randomHeight % 2 == 0){
            randomHeight = random.nextInt(HEIGHT);
        }
        int randomWidth = random.nextInt(WIDTH);
        while (randomWidth % 2 == 0){
            randomWidth = random.nextInt(WIDTH);
        }
        maze[randomHeight][randomWidth] = "empty";
        dfsRecursion(randomHeight, randomWidth);

        makeLoop();

        //ensures there are outer walls
        for (int i = 0; i < HEIGHT; i++){
            maze[i][19] = "wall";
            maze[i][0] = "wall";
            setVisited(i, 19);
            setVisited(i, 0);
        }
        for (int j = 0; j < WIDTH; j++){
            maze[0][j] = "wall";
            maze[14][j] = "wall";
            setVisited(0, j);
            setVisited(14, j);
        }

        //ensures 4 corners are empty for mouse and cats
        maze[1][1] = "mouse";
        maze[1][18] = "cat";
        maze[13][1] = "cat";
        maze[13][18] = "cat";
        return maze;
    }
    public static void dfsRecursion(int randomHeight, int randomWidth){
        RandomDirection direction = new RandomDirection();
        direction.shuffleDirection();
        List<String> randomDirection = direction.getRandomDirection();

        for (int i = 0; i < randomDirection.size(); i++){
            switch(randomDirection.get(i)){
                case "up":
                    if (randomHeight - 2 <= 0) {
                        continue;
                    }
                    if (maze[randomHeight - 2][randomWidth] == "wall") {
                        maze[randomHeight - 2][randomWidth] = "empty";
                        maze[randomHeight - 1][randomWidth] = "empty";
                        dfsRecursion(randomHeight - 2, randomWidth);
                    }
                    break;
                case "down":
                    if (randomHeight + 2 >= HEIGHT - 1){
                        continue;
                    }
                    if (maze[randomHeight + 2][randomWidth] == "wall"){
                        maze[randomHeight + 2][randomWidth] = "empty";
                        maze[randomHeight + 1][randomWidth] = "empty";
                        dfsRecursion(randomHeight + 2, randomWidth);
                    }
                    break;
                case "right":
                    if (randomWidth + 2 >= WIDTH - 1){
                        continue;
                    }
                    if (maze[randomHeight][WIDTH - 1] == "wall"){
                        maze[randomHeight][WIDTH - 2] = "empty";
                    }
                    if (maze[randomHeight][randomWidth + 2] == "wall"){
                        maze[randomHeight][randomWidth + 2] = "empty";
                        maze[randomHeight][randomWidth + 1] = "empty";
                        dfsRecursion(randomHeight, randomWidth + 2);
                    }
                    break;
                case "left":
                    if (randomWidth - 2 <= 0){
                        continue;
                    }
                    if (maze[randomHeight][randomWidth - 2] == "wall"){
                        maze[randomHeight][randomWidth - 2] = "empty";
                        maze[randomHeight][randomWidth - 1] = "empty";
                        dfsRecursion(randomHeight, randomWidth - 2);
                    }
                    break;
            }
        }
    }

    public static void makeLoop(){
        int numLoops = 20;
        int count = 0;
        for (int i = 0; i < numLoops; i++) {
            int randomHeight = random.nextInt(HEIGHT - 2) +1 ;
            int randomWidth = random.nextInt(WIDTH - 2) + 1;

            if (maze[randomHeight][randomWidth] == "wall") {
                if ((maze[randomHeight][randomWidth - 1] == "wall") && (maze[randomHeight][randomWidth + 1] == "wall")) {
                    maze[randomHeight][randomWidth] = "empty";
                    count++;
                }
            }
            if (count == 5){
                return;
            }
        }
    }
}
