package optProblem;

import java.util.Arrays;

/** @author antonio This class is for the Map. */
public class Map implements IMap {

    // ATTRIBUTES - TALVES DEPOIS PASSAR PARA AQUI AS CENAS ACERCA DO MAPA??
    int cmax;
    Point mapDimensions;
    Point[] obstacles;
    SpecialZone[] specialZones;
    int total_obst;
    int number_zones;

    // CONTRUCTORS
    public Map(Point p) {
        mapDimensions = p;
    }

    public Map(int xx, int yy, int no, int cmaxx) {
        cmax = cmaxx;
        obstacles = new Point[no];
        mapDimensions = new Point(xx, yy);
    }

    // METHODS

    /**
     * Method that lets us know if a move is valid or not. If the move is not valid then we return
     * true, false is returned otherwise. Quick reminder that 0 = move up, 1 = move right, 2 = move
     * down, 3 = move left
     *
     * @param actPoint is the point in which the individual is right now.
     * @param index is the indicator of where we want to move.
     * @param obstacles is the array of obstacles - points that we cannot transverse.
     * @return is true if the move was invalid and false if the move is valid.
     */
    public boolean nonValidMove(Point actPoint, int index, Point[] obstacles) {

        // check if its outside map boundaries
        if (actPoint.x == mapDimensions.x && index == 1) return true;
        if (actPoint.x == 1 && index == 3) return true;
        if (actPoint.y == 1 && index == 2) return true;
        if (actPoint.y == mapDimensions.y && index == 0) return true;

        // check if its in obstacles list
        if (obstacles == null) return false;
        if (index == 0 && Arrays.asList(obstacles).contains(new Point(actPoint.x, actPoint.y + 1)))
            return true;
        if (index == 1 && Arrays.asList(obstacles).contains(new Point(actPoint.x + 1, actPoint.y)))
            return true;
        if (index == 2 && Arrays.asList(obstacles).contains(new Point(actPoint.x, actPoint.y - 1)))
            return true;
        if (index == 3 && Arrays.asList(obstacles).contains(new Point(actPoint.x - 1, actPoint.y)))
            return true;

        return false;
    }
    
    public Point getDimensions() {
    	return this.mapDimensions;
    }

    public void addObstacle(Point p) {
        obstacles[total_obst] = p;
        total_obst++;
    }

    public void addObstacles(Point[] obsts) {
        obstacles = obsts;
        total_obst = obstacles.length;
    }

    public void addSpecialZones(SpecialZone[] specials) {
        specialZones = specials;
        number_zones = specialZones.length;
    }

    public void addMaxCost(int c) {
        cmax = c;
    }

    /**
     * This method is a static method since it is not invoked from any Map object. It gets a pair of
     * adjacent points and returns the cost associated with the edge between them.
     *
     * @param actPoint is the point in which the Individual is right now.
     * @param newPoint is the point to which the Individual wants to go.
     * @param specialZones is the List of parts of the map with special cost.
     * @return is an integer with the cost of transversing the from actPoint to newPoint
     */
    public int getCost(Point actPoint, Point newPoint, SpecialZone[] specialZones) {

        int x1, x2, y1, y2;
        int nx = newPoint.x;
        int ny = newPoint.y;
        int ax = actPoint.x;
        int ay = actPoint.y;

        for (SpecialZone ed : specialZones) {
            x1 = ed.p1.x;
            x2 = ed.p2.x;
            y1 = ed.p1.y;
            y2 = ed.p2.y;
            // if we are coming from inside the zone to another point inside the zone we want to
            // count the
            // limits
            if ((ax >= x1 && ax <= x2)
                    && (ay >= y1 && ay <= y2)
                    && (nx >= x1 && nx <= x2)
                    && (ny >= y1 && ny <= y2)) {
                return ed.cost;
            }
            // if we are coming from OUTSIDE the zone to a point inside the zone we DONT want to
            // count the
            // limits
            else if ((nx > x1 && nx < x2) && (ny > y1 && ny < y2)) {
                return ed.cost;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return "Map [cmax="
                + cmax
                + ", mapDimensions="
                + mapDimensions
                + ", obstacles="
                + Arrays.toString(obstacles)
                + ", specialZones="
                + specialZones
                + ", total_obst="
                + total_obst
                + ", number_zones="
                + number_zones
                + "]";
    }
}
