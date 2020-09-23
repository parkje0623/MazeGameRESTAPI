package ca.cmpt213.as4.restapi;

import ca.cmpt213.as4.Model.Maze;
import java.util.List;

public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiBoardWrapper makeFromGame(Maze game) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.hasWalls = new boolean[game.getHeight()][game.getWidth()];
        wrapper.isVisible = new boolean[game.getHeight()][game.getWidth()];

        // Populate this object!
        wrapper.boardHeight = game.getHeight();
        wrapper.boardWidth = game.getWidth();
        for (int i = 0; i < game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                String locationInfo = game.getMazeInfo(i, j);
                wrapper.hasWalls[i][j] = false;
                if (locationInfo.equals("mouse")) {
                    wrapper.mouseLocation = ApiLocationWrapper.makeFromCellLocation(j, i);
                } else if (locationInfo.equals("cat")) {
                    wrapper.catLocations = ApiLocationWrapper.makeFromCellLocations(j, i);
                } else if (locationInfo.equals("wall")) {
                    wrapper.hasWalls[i][j] = true;
                }

                if (game.getVisited()[i][j]) {
                    wrapper.isVisible[i][j] = true;
                } else {
                    wrapper.isVisible[i][j] = false;
                }
            }
        }
        return wrapper;
    }
}