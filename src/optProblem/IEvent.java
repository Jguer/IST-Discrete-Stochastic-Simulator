package optProblem;

/**
 * Interface for the event classes.
 * 
 * @author group2
 *     <p>IEvent is used in the <code>PEC</code> and most specific to the <code>PriorityQueue</code>
 *     {@link pec.PriorityQueuePec}
 */
public interface IEvent {

    public abstract void ExecEvent(OptProblem op);
}
