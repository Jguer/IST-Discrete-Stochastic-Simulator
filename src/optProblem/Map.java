package optProblem;

import java.util.Arrays;

/**
 * @author group2
 *     <p>This class is for the Map. All the attributes are map related. Apart from the methods that
 *     implement the ones in the interface there was a net to include some setters/getters because
 *     of the use of this class outside this package.
 */
public class Map implements IMap {

    // ATTRIBUTES - TALVES DEPOIS PASSAR PARA AQUI AS CENAS ACERCA DO MAPA??
    int cmax;
    Point mapDimensions;
    Point[] obstacles;
    SpecialZone[] specialZones;
    int total_obst;
    int number_zones;

    // CONTRUCTORS

    /**
     * Constructor of the Map object. This is a simpler one that only receives a Point and adds it
     * to the map dimensions field.
     *
     * @param p is the Point containing information about the map dimensions
     */
    public Map(Point p) {
        mapDimensions = p;
    }

    /**
     * Constructor of the Map object. This is a more advanced one that can be used to initialize
     * several fields.
     *
     * @param xx is the number of columns in the map
     * @param yy is the number of rows in the map
     * @param no is the number of obstacles in the map
     * @param cmaxx is the maximum cost of an edge
     */
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
        if (actPoint.getX() == mapDimensions.getX() && index == 1) return true;
        if (actPoint.getX() == 1 && index == 3) return true;
        if (actPoint.getY() == 1 && index == 2) return true;
        if (actPoint.getY() == mapDimensions.getY() && index == 0) return true;

        // check if its in obstacles list
        if (obstacles == null) return false;
        Point toCompare = null;
        switch (index) {
            case 0:
                toCompare = new Point(actPoint.getX(), actPoint.getY() + 1);
                break;
            case 1:
                toCompare = new Point(actPoint.getX() + 1, actPoint.getY());
                break;
            case 2:
                toCompare = new Point(actPoint.getX(), actPoint.getY() - 1);
                break;
            case 3:
                toCompare = new Point(actPoint.getX() - 1, actPoint.getY());
                break;
        }
        if (Arrays.asList(obstacles).contains(toCompare)) return true;

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
        int nx = newPoint.getX();
        int ny = newPoint.getY();
        int ax = actPoint.getX();
        int ay = actPoint.getY();

        for (SpecialZone ed : specialZones) {
            x1 = ed.p1.getX();
            y1 = ed.p1.getY();
            x2 = ed.p2.getX();
            y2 = ed.p2.getY();
            // if we are coming from inside the zone to another point inside the zone we want to
            // count the
            // limits
            if ((ax >= x1 && ax <= x2)
                            && (ay >= y1 && ay <= y2)
                            && (nx >= x1 && nx <= x2)
                            && (ny >= y1 && ny <= y2)
                    || ((nx > x1 && nx < x2) && (ny > y1 && ny < y2))) {
                return ed.cost;
            }
        }
        return 1;
    }

    /** To String method that overrides the one with the same name in the Object superclass. */
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
