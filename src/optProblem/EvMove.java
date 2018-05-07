package optProblem;

import java.util.List;
import java.util.Random;

/**
 * @author group2
 *     <p>This subclass is an extension of the abstract Event class and will handle the Move events.
 *     It has no fields on its own but inherits time and individual from the superclass. It needs to
 *     specify the Execute Event and toString methods.
 */
public class EvMove extends Event {

    // ATTRIBUTES

    // CONTRUCTORS
    /**
     * This is the constructor. All it does is call the superclass' constructor with the arguments
     * provided.
     *
     * @param timee is the time of the move.
     * @param ind is the Individual associated with this event.
     */
    public EvMove(double timee, Individual ind) {
        super(timee, ind);
    }

    // METHODS
    /**
     * This method is a redefinition of the general Event method with the same name.
     *
     * @see optProblem.Event#ExecEvent(optProblem.OptProblem) for the default documentation. In this
     *     particular case we move the individual in the event to one of the valid adjacent points.
     *     We need to check for cycles. If we detect one then the path between the last time this
     *     point was added to the history and this point will be deleted. If the goal point was in
     *     that portion of the history then the hit boolean is set to zero.
     */
    public void ExecEvent(OptProblem opp) {

        StochasticOptProblem op = (StochasticOptProblem) opp;

        op.num_moves++;

        Individual ind = this.getIndividual();

        // create new move and only add if happens before death
        double randTime = op.actual_time + Event.expRandom(ind.getValueForExpMean() * op.move_mean);
        if (randTime < ind.death_time) {
            op.pec.addElement(new EvMove(randTime, ind), ec);
        }

        Point actPoint = ind.history.get(ind.history.size() - 1);

        Random r = new Random();
        int index = r.nextInt(4);
        // 0 --> move up, 1 --> move right, 2 --> move down, 3 --> move left
        while (op.map.nonValidMove(actPoint, index, op.map.obstacles)) {
            index = r.nextInt(4); // can be improved probably. usually is pretty fast but sometimes
            // can take a while
            // to get a good index so yeah
        }

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

        index = Point.findSamePoint(ind.history, toCompare);
        if (index
                != -1) { // if this point closes a cycle --> delete the cycle and remove those costs
            List<Integer> subCosts = ind.costs.subList(index + 1, ind.costs.size());
            List<Point> subPoints = ind.history.subList(index + 1, ind.history.size());

            // if we are going to remove a cycle that contains the goal than this individual will no
            // longer have hit the goal
            if (Point.findSamePoint(subPoints, op.goal) != -1) ind.hit = false;

            int cycle_cost = 0;
            for (int i = 0; i < subCosts.size(); i++) {
                cycle_cost += subCosts.get(i);
            }
            // delete these lists and the points/costs in the ind.history ones that have the same
            // place in memory
            subPoints.clear();
            subCosts.clear();
            ind.cost -= cycle_cost;

        } else { // in case this point didnt close a cycle then we want to add it normally

            ind.history.add(toCompare);
            int current_cost = op.map.getCost(actPoint, toCompare, op.map.specialZones);
            ind.cost += current_cost;
            ind.costs.add(current_cost);

            if (toCompare.equals(op.goal)) {
                ind.hit = true;
                op.hit = true;
            }
        }
    }

    /**
     * To String method that overrides the one with the same name in the Object superclass. Since
     * this class extends a superclass that is abstract and has this as a method then this
     * redefinition is mandatory.
     */
    public String toString() {
        return ("(Type:Move,Ind ID:" + this.getIndividual().identifier + ",Time:" + this.getTime() + ")");
    }
}
