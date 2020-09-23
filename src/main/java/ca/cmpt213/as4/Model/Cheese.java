package ca.cmpt213.as4.Model;

/**
 * class for cheese to store and provide the position of cheese(row, col)
 */

public class Cheese {
    private int rowPosition;
    private int colPosition;

    public Cheese(int rowPosition, int colPosition) {
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColPosition() {
        return colPosition;
    }

}
