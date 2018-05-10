package optProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Individual's Reproduction Event
 * 
 * @author group2
 *     <p>This subclass is an extension of the abstract Event class and will handle the Reproduction
 *     events. It has no fields on its own but inherits time and individual from the superclass. It
 *     needs to specify the Execute Event and toString methods.
 */
public class EvRepr extends Event {

    // ATTRIBUTES

    // CONTRUCTORS
    /**
     * This is the constructor. All it does is call the superclass' constructor with the arguments
     * provided.
     *
     * @param timee is the time of the reproduction.
     * @param ind is the Individual associated with this event.
     */
    public EvRepr(double timee, Individual ind) {
        super(timee, ind);
    }

    // METHODS
    /**
     * This method is a redefinition of the general Event method with the same name.
     *
     * @see optProblem.Event#ExecEvent(optProblem.OptProblem) for the default documentation. In this
     *     particular case we have 2 individuals involved. One of them is the parent and the other
     *     will be the son. For the parent we create a new reproduction and add it to the PEC if the
     *     time is lower than the death time of the individual. For the son we create the history
     *     based on the father's and then update its comfort and add the Death, Reproduction and
     *     Move events to the PEC.
     */
    public void ExecEvent(OptProblem opp) {

        StochasticOptProblem op = (StochasticOptProblem) opp;

        op.setNum_reprs(op.getNum_reprs() + 1);

        // create "son" individual
        Individual ind = new Individual(op.getTotalIndividuals());
        op.setTotalIndividuals(op.getTotalIndividuals() + 1);
        op.setAliveIndividuals(op.getAliveIndividuals() + 1);
        op.getIndividualsList().add(ind);

        // System.out.println("REPRODUCTION!!");
        // System.out.println(op.actual_time);
        Individual father = this.getIndividual();

        // create new reproduction for the father - only add if happens before its death
        double randTime =
                op.getActual_time()
                        + Event.expRandom(father.getValueForExpMean(2) * op.getRepr_mean());
        if (randTime < father.getDeathTime()) {
            op.getPec().addElement(new EvRepr(randTime, father), ec);
        }
        

        int father_hist_size = father.getHistory().size();
        int prefix_size =
                (int) Math.ceil(father_hist_size * (0.9 + 0.1 * father.getComfort()));
        List<Point> newhist = Point.clonePointList(father.getHistory());
        List<Integer> newcosts = new ArrayList<Integer>();
        newcosts.addAll(father.getCosts());
        
        //if there are not enough points in the path then the son will automatically have the father's path (because ceil of 90% path = path)
        if(prefix_size >= father.getHistory().size()) {
        	//do nothing since the son's path will be equal to the parents path
        }
        //otherwise we can let the son have only 90% + comfort%
        else {            
            newhist.subList(prefix_size + 1, newhist.size()).clear();
            newcosts.subList(prefix_size + 1, newcosts.size()).clear();
        }
        
        //update the son's history and costs with the ones from the already cutout new lists
        ind.getHistory().addAll(newhist); 
        ind.getCosts().addAll(newcosts);

        // System.out.println("pre " + prefix_size + " histFather " + father_hist_size + "
        // newhist " +
        // newhist.size());

        //calculating the sons total cost based on its costs
        int cycle_cost = 0;
        for (int i = 1; i < newcosts.size(); i++) {
            cycle_cost += newcosts.get(i);
        }
        ind.setCost(cycle_cost);

        //checking if the son has passed the goal (sometimes the parent can have hit but we cut those steps from the son's path
        if (Point.findSamePoint(newhist, op.getGoal()) != -1)
        	ind.setHit(true);
        else
        	ind.setHit(false);


        //update comfort
        ind.updateComfort(op.getGoal(), op.getMap(), op.getK());
        
        //create death event!
        ind.setDeathTime(
                op.getActual_time()
                        + Event.expRandom(ind.getValueForExpMean(1) * op.getDeath_mean()));
        op.getPec().addElement(new EvDeath(ind.getDeathTime(), ind), ec);

        // create move and only add if happens before death
        randTime =
                op.getActual_time() + Event.expRandom(ind.getValueForExpMean(2) * op.getMove_mean());
        if (randTime < ind.getDeathTime()) {
            op.getPec().addElement(new EvMove(randTime, ind), ec);
        }

        // create reproduction and only add if happens before death
        randTime =
                op.getActual_time() + Event.expRandom(ind.getValueForExpMean(2) * op.getRepr_mean());
        if (randTime < ind.getDeathTime()) {
            op.getPec().addElement(new EvRepr(randTime, ind), ec);
        }
    }

    /**
     * To String method that overrides the one with the same name in the Object superclass. Since
     * this class extends a superclass that is abstract and has this as a method then this
     * redefinition is mandatory.
     */
    public String toString() {
        return ("(Type:Repr,Ind ID:"
                + this.getIndividual().getIdentifier()
                + ",Time:"
                + this.getTime()
                + ")");
    }
}
