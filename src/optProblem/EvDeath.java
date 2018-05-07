package optProblem;

/**
 * @author group2
 * 
 * This subclass is an extension of the abstract Event class and will handle the
 * Death events. It has no fields on its own but inherits time and individual from the
 * superclass. It needs to redefine the Execute Event and toString methods.
 */
public class EvDeath extends Event {

    // ATTRIBUTES

    // CONTRUCTORS
    /**
     * This is the constructor. All it does is call the superclass' constructor with the arguments
     * provided.
     *
     * @param timee is the time of the death.
     * @param ind is the Individual associated with this event.
     */
    public EvDeath(double timee, Individual ind) {
        super(timee, ind);
    }

    // METHODS
    /**
     * This method is a redefinition of the general Event method with the same name.
     *
     * @see optProblem.Event#ExecEvent(optProblem.OptProblem) for the default documentation. In this
     *     particular case we kill the individual in the event. By killing we mean removing it from
     *     the list of individuals and decreasing the number of alive individuals by 1.
     */
    public void ExecEvent(OptProblem opp) {

        StochasticOptProblem op = (StochasticOptProblem) opp;

        op.num_deaths++;

        op.alive_inds--;
        op.list_inds.remove(this.individual);
    }

    /**
     * To String method that overrides the one with the same name in the Object superclass. Since
     * this class extends a superclass that is abstract and has this as a method then this
     * redefinition is mandatory.
     */
    public String toString() {
        return ("(Type:Death,Ind ID:" + this.individual.identifier + ",Time:" + this.time + ")");
    }
}
