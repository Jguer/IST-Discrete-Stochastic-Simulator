package optProblem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 * @author group2
 * 
 * This subclass is an extension of the abstract Event class and will handle the
 * Epidemic events. It inherits time and individual from the
 * superclass. Individual is set to null and time is the actual time so it happens in the next
 * iteration of the simulation. The static field is an Individual Comparator. It needs to
 * specify an implementation for the the Execute Event and toString methods.
 */
public class EvEpidemic extends Event {

    // ATTRIBUTES
    
    // CONTRUCTORS
    /**
     * This is the constructor. All it does is call the superclass' constructor with the arguments
     * provided. Individual is always null since it doesn't target any specific individual.
     *
     * @param op is the Optimization Problem in question. This way we can get the actual time
     *     instant.
     */
    public EvEpidemic(StochasticOptProblem op) {
        super(op.actual_time, null);
    }

    // METHODS
    /**
     * This method is a redefinition of the general Event method with the same name.
     *
     * @see optProblem.Event#ExecEvent(optProblem.OptProblem) for the default documentation. In this
     *     particular case we have to keep the 5 individuals with best comfort and decide whether or
     *     not to kill the others using a random variable and the individual's comfort. If an
     *     individual is killed in an epidemic then all the events in the PEC associated to the
     *     individual must be removed.
     */
    public void ExecEvent(OptProblem opp) {

        StochasticOptProblem op = (StochasticOptProblem) opp;

        op.num_epidemics++;

        double r;

        // this is done so we DON'T ever kill the best 5 in an epidemic
        List<Individual> newinds = Individual.cloneIndividualList(op.list_inds);
        newinds.sort(Individual.ic); // System.out.println(op.alive_inds + " " +newinds.toString());
        List<Individual> subinds = newinds.subList(5, newinds.size());

        // System.out.println(op.actual_time);
        // System.out.println("ANTES: " + op.pec.toString());

        for (Individual ind : subinds) {
            r = Event.r.nextDouble();
            if (r > ind.comfort) { // sorry but you will die, says the programmer to the individual

                Collection<Event> pecevents = (Queue<Event>) op.pec.getElementList();
                List<Event> found = new ArrayList<Event>();

                // remove all events in the pec that are related to the individual we are killing
                for (Event ev : pecevents) {
                    Individual indd = ev.individual;
                    if (indd != null && indd.sameIdentifier(ind)) {
                        found.add(ev);
                    }
                }
                pecevents.removeAll(found);
                op.list_inds.remove(ind);
            }
        }

        op.alive_inds = op.list_inds.size();
        // System.out.println("\nDEPOIS: " + op.pec.toString());
        // System.out.println(op.alive_inds + " " + op.list_inds.toString());
    }

    /**
     * To String method that overrides the one with the same name in the Object superclass. Since
     * this class extends a superclass that is abstract and has this as a method then this
     * redefinition is mandatory.
     */
    public String toString() {
        return ("(Type:Epidemic - No time & No ind.)");
    }
}
