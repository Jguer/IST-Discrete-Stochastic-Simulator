package optProblem;

import java.util.Random;

/**
 * @author antonio Abstract class for Events. Has a field for the associated individual and one for
 *     the time in which we should execute the event. There are 2 static fields. The first is a
 *     Random object to generate random numbers and the other is an Event Comparator that is used
 *     when we want to add events to the PEC. Execute Event and toString methods must be overridden.
 */
public abstract class Event implements IEvent {

    // ATTRIBUTES
    Individual individual;
    double time;

    public static Random r = new Random();
    public static EventComparator ec = new EventComparator();

    // CONTRUCTORS
    /**
     * Constructor of the Event class. Since Event is abstract this constructor will have to be
     * called from the subclasses using this()
     *
     * @param timee is the time of the event
     * @param ind is the Individual we want to associate with this event
     */
    public Event(double timee, Individual ind) {
        time = timee;
        individual = ind;
    }

    // METHODS
    /**
     * Abstract method of the event execution. Each event type should redefine this method to do
     * what it should do.
     *
     * @param op is the Optimization Problem that this event belongs to.
     */
    public abstract void ExecEvent(OptProblem op);

    /**
     * To String method that overrides the one with the same name in the Object superclass.
     *
     * @see java.lang.Object#toString() for the default JavaDoc.
     */
    public abstract String toString();

    /**
     * Method that returns a random number according to an exponential pdf with mean m. This method
     * doesn't depend on who invokes it so it is static and it will be invoked from the class
     * itself.
     *
     * @param m is the mean we want to use in the exponential.
     * @return is the random number generated.
     */
    public static double expRandom(double m) {
        double next = Event.r.nextDouble();
        return -m * Math.log(1.0 - next);
    }
}
