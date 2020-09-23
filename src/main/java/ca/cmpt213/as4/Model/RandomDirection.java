package ca.cmpt213.as4.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * class for creating an array of random directions {up, down, left, right}.
 * Used for randomly moving cats and generating the maze
 */

public class RandomDirection {
    private String[] directions = {"up", "down", "left", "right"};
    private List<String> randomDirection = Arrays.asList(directions);

    public RandomDirection(){
        this.randomDirection = randomDirection;
    }

    public void shuffleDirection() {
        Collections.shuffle(randomDirection);
    }

    public List<String> getRandomDirection() {
        return randomDirection;
    }
}
