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

        op.setNum_moves(op.getNum_moves() + 1);

        Individual ind = this.getIndividual();

        // create new move and only add if happens before death
        double randTime = op.getActual_time() + Event.expRandom(ind.getValueForExpMean() * op.getMove_mean());
        if (randTime < ind.getDeathTime()) {
            op.getPec().addElement(new EvMove(randTime, ind), ec);
        }

        Point actPoint = ind.getHistory().get(ind.getHistory().size() - 1);

        Random r = new Random();
        int index = r.nextInt(4);
        // 0 --> move up, 1 --> move right, 2 --> move down, 3 --> move left
        while (op.getMap().nonValidMove(actPoint, index, op.getMap().getObstacles())) {
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

        index = Point.findSamePoint(ind.getHistory(), toCompare);
        if (index
                != -1) { // if this point closes a cycle --> delete the cycle and remove those costs
            List<Integer> subCosts = ind.getCosts().subList(index + 1, ind.getCosts().size());
            List<Point> subPoints = ind.getHistory().subList(index + 1, ind.getHistory().size());

            // if we are going to remove a cycle that contains the goal than this individual will no
            // longer have hit the goal
            if (Point.findSamePoint(subPoints, op.getGoal()) != -1) ind.setHit(false);

            int cycle_cost = 0;
            for (int i = 0; i < subCosts.size(); i++) {
                cycle_cost += subCosts.get(i);
            }
            // delete these lists and the points/costs in the ind.history ones that have the same
            // place in memory
            subPoints.clear();
            subCosts.clear();
            ind.setCost(ind.getCost() - cycle_cost);

        } else { // in case this point didnt close a cycle then we want to add it normally

            ind.getHistory().add(toCompare);
            int current_cost = op.getMap().getCost(actPoint, toCompare, op.getMap().specialZones);
            ind.setCost(ind.getCost() + current_cost);
            ind.getCosts().add(current_cost);

            if (toCompare.equals(op.getGoal())) {
                ind.setHit(true);
                op.setHit(true);
            }
        }
    }

    /**
     * To String method that overrides the one with the same name in the Object superclass. Since
     * this class extends a superclass that is abstract and has this as a method then this
     * redefinition is mandatory.
     */
    public String toString() {
        return ("(Type:Move,Ind ID:" + this.getIndividual().getIdentifier() + ",Time:" + this.getTime() + ")");
    }
}
