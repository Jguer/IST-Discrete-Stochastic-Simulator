package optProblem;

/**
 * @author group2
 *     <p>This class is for Special Zones of the map, i.e.,the two points that delimit a rectangular
 *     area and the cost associated to moving in the said zone.
 */
public class SpecialZone {

    // ATTRIBUTES
    Point p1;
    Point p2;
    int cost;

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

    @Override
    public String toString() {
        return "SpecialZone [p1=" + p1 + ", p2=" + p2 + ", cost=" + cost + "]";
    }

    // METHODS

}
