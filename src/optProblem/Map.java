package optProblem;

import java.util.Arrays;

/**
 * @author group2
 *     <p>This class is for the Map. All the attributes are map related. Apart from the methods that
 *     implement the ones in the interface there was a net to include some setters/getters because
 *     of the use of this class outside this package.
 */
public class Map implements IMap {

    // ATTRIBUTES
    private int cmax;
    private Point mapDimensions;
    private Point[] obstacles;
    SpecialZone[] specialZones;
    private int total_obst;
    private int number_zones;

    // CONTRUCTORS

    /**
     * Constructor of the Map object. This is a simpler one that only receives a Point and adds it
     * to the map dimensions field.
     *
     * @param p is the Point containing information about the map dimensions
     */
    public Map(Point p) {
        setMapDimensions(p);
    }

    /**
     * Constructor of the Map object. This is a more advanced one that can be used to initialize
     * several fields.
     *
     * @param xx is the number of columns in the map
     * @param yy is the number of rows in the map * @param no is the number of obstacles in the map
     * @param cmaxx is the maximum cost of an edge
     */
    public Map(int xx, int yy, int no, int cmaxx) {
        setCmax(cmaxx);
        setObstacles(new Point[no]);
        setMapDimensions(new Point(xx, yy));
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
        if (actPoint.getX() == getMapDimensions().getX() && index == 1) return true;
        if (actPoint.getX() == 1 && index == 3) return true;
        if (actPoint.getY() == 1 && index == 2) return true;
        if (actPoint.getY() == getMapDimensions().getY() && index == 0) return true;

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
        return this.getMapDimensions();
    }

    public void addObstacle(Point p) {
        getObstacles()[total_obst] = p;
        total_obst++;
    }

    public void addObstacles(Point[] obsts) {
        setObstacles(obsts);
        total_obst = getObstacles().length;
    }

    public void addSpecialZones(SpecialZone[] specials) {
        specialZones = specials;
        number_zones = specialZones.length;
    }

    public void addMaxCost(int c) {
        setCmax(c);
    }

    /**
     * This method returns the cost associated with the edge between them.
     *
     * @param actPoint is the point in which the Individual is right now.
     * @param newPoint is the point to which the Individual wants to go.
     * @param specialZones is the List of parts of the map with special cost.
     * @return is an integer with the cost of transversing the from actPoint to newPoint
     */
    public int getCost(Point actPoint, Point newPoint, SpecialZone[] specialZones) {
        for (SpecialZone ed : specialZones) {
            int cost = ed.getCost(newPoint, actPoint);
            if (cost != -1) {
                return cost;
            }
        }
        return 1;
    }

    /** To String method that overrides the one with the same name in the Object superclass. */
    public String toString() {
        return "Map [cmax="
                + getCmax()
                + ", mapDimensions="
                + getMapDimensions()
                + ", obstacles="
                + Arrays.toString(getObstacles())
                + ", specialZones="
                + specialZones
                + ", total_obst="
                + total_obst
                + ", number_zones="
                + number_zones
                + "]";
    }

    /** @return the mapDimensions */
    public Point getMapDimensions() {
        return mapDimensions;
    }

    /** @param mapDimensions the mapDimensions to set */
    public void setMapDimensions(Point mapDimensions) {
        this.mapDimensions = mapDimensions;
    }

    /** @return the cmax */
    public int getCmax() {
        return cmax;
    }

    /** @param cmax the cmax to set */
    public void setCmax(int cmax) {
        this.cmax = cmax;
    }

    /** @return the obstacles */
    public Point[] getObstacles() {
        return obstacles;
    }

    /** @param obstacles the obstacles to set */
    public void setObstacles(Point[] obstacles) {
        this.obstacles = obstacles;
    }
}
