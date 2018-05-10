package optProblem;

/**
 * Interface for the Map classes.
 * 
 * @author group2
 *     <p>IMAP is used in the <code>StochasticOptProblem</code>. The only required methods are the
 *     nonValidMove that should return true is a move is NOT valid and false otherwise and the
 *     getCost that given the starting point and end point of a move returns the cost of that
 *     passage. {@link optProblem.StochasticOptProblem}
 */
public interface IMap {

    public boolean nonValidMove(Point actPoint, int index, Point[] obstacles);

    public int getCost(Point actPoint, Point newPoint, SpecialZone[] specialZones);
}
