package ca.cmpt213.as4.restapi;

import ca.cmpt213.as4.Model.Maze;
import java.util.ArrayList;
import java.util.List;

public class ApiLocationWrapper {
    public int x;
    public int y;
    public static List<ApiLocationWrapper> locations = new ArrayList<>();

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiLocationWrapper makeFromCellLocation(int heightCell, int widthCell) {
        ApiLocationWrapper location = new ApiLocationWrapper();

        // Populate this object!
        location.x = widthCell;
        location.y = heightCell;

        return location;
    }
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static List<ApiLocationWrapper> makeFromCellLocations(int heightCell, int widthCell) {
        ApiLocationWrapper location = new ApiLocationWrapper();
        // Populate this object!
        if (locations.size() == 3) {
            locations = new ArrayList<>();
        }
        location.x = heightCell;
        location.y = widthCell;
        locations.add(location);

        return locations;
    }
}