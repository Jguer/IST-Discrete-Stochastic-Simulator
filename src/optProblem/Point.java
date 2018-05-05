package optProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author antonio This class will store a Point, i.e., a pair of (x,y) values. No attributes are
 *     static so each object will have their own. Equals, Hashcode and toString were overridden.
 */
public class Point {

    // ATTRIBUTES
    int x;
    int y;

    // CONTRUCTORS
    /**
     * Constructor for the Point Class. It should only receive 2 integers and will associate each to
     * the corresponding point in the class.
     *
     * @param xx is the x value to be stored
     * @param yy is the y value to be stored
     */
    public Point(int xx, int yy) {
        x = xx;
        y = yy;
    }

    // METHODS
    /**
     * Hashcode method that overrides the one with the same name in the Object superclass. Was done
     * to complement the changes done to the equals method.
     *
     * @see java.lang.Object#hashCode() for the original JavaDocs.
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
     * Equals method that overrides the one with the same name in the Object superclass.
     *
     * @param obj is the object to be compared with the one invoking the method.
     * @see java.lang.Object#equals(java.lang.Object) for the original JavaDocs.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Point other = (Point) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        return true;
    }

    /**
     * To String method that overrides the one with the same name in the Object superclass.
     *
     * @see java.lang.Object#toString() for the default JavaDoc.
     */
    @Override
    public String toString() {
        return "(" + x + ',' + y + ")";
    }

    // returns the index of that same point in the array or -1 if it isn't there
    /**
     * Method that returns the index of the list in which we can find the point passed as argument.
     * In case the said point wasn't found it will return -1.
     *
     * @param points is the list of points where we want to search
     * @param point is the point we want to find
     * @return is the index in which we found the point or -1 in case it wasn't found
     */
    public static int findSamePoint(List<Point> points, Point point) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).equals(point)) {
                return i;
            }
        }
        return -1;
    }
    
    public int getX() {
    	return this.x;
    }
    
    public int getY() {
    	return this.y;
    }

    /**
     * Method to clone an Array List of Points. The clone relies on the Constructor and is done in a
     * for loop iterating over the points in the list. This way we get 2 lists in memory, each with
     * their own objects. If we change one of them it will not change the other.
     *
     * @param list is the original list we want to clone
     * @return is the new list, a clone of the original one
     */
    public static List<Point> clonePointList(List<Point> list) {
        List<Point> clone = new ArrayList<Point>(list.size());
        for (Point item : list) clone.add(new Point(item.x, item.y));
        return clone;
    }
}
