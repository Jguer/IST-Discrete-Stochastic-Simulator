package optProblem;

/**
 * @author group2
 *     <p>This class is for Special Zones of the map, i.e.,the two points that delimit a rectangular
 *     area and the cost associated to moving in the said zone.
 */
public class SpecialZone {

    // ATTRIBUTES
    private Point p1;
    private Point p2;
    private int cost;

    // CONTRUCTORS
    /**
     * This method is the constructor for the SpecialZone. All it does is associate the points and
     * cost with the variables of the SpecialZone object.
     *
     * @param p11 is the point that marks the lower left corner of the zone.
     * @param p22 is the point that marks the upper right corner of the zone.
     * @param cc is the cost associated with this zone.
     */
    public SpecialZone(Point p11, Point p22, int cc) {
        p1 = p11;
        p2 = p22;
        cost = cc;
    }

    // METHODS
    @Override
    public String toString() {
        return "SpecialZone [p1=" + p1 + ", p2=" + p2 + ", cost=" + cost + "]";
    }
    
    /**
     * @param actP is the point in which the Individual is right now.
     * @param newP is the point to which the Individual wants to go.
     * @return cost if it's inside zone, otherwise return -1 for not found
     */
    public int getCost(Point newP, Point actP) {
			int nx = newP.getX();
			int ny = newP.getY();
			int ax = actP.getX();
			int ay = actP.getY();
            int x1 = p1.getX();
            int y1 = p1.getY();
            int x2 = p2.getX();
            int y2 = p2.getY();
            // if we are coming from inside the zone to another point inside the zone we want to
            // count the
            // limits
            if ((ax >= x1 && ax <= x2)
                            && (ay >= y1 && ay <= y2)
                            && (nx >= x1 && nx <= x2)
                            && (ny >= y1 && ny <= y2)
                    || ((nx > x1 && nx < x2) && (ny > y1 && ny < y2))) {
                return cost;
            }
    	
    	return -1;
    }

}
