package ca.cmpt213.as4.restapi;

import ca.cmpt213.as4.Model.GameManager;

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public boolean eatenCheese;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiGameWrapper makeFromGame(GameManager game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;
        wrapper.isGameLost = false;
        wrapper.isGameWon = false;
        wrapper.eatenCheese = false;

        // Populate this object!
        wrapper.numCheeseFound = game.getNumCheeseCollected();
        wrapper.numCheeseGoal = game.getNumCheese();
        return wrapper;
    }
}