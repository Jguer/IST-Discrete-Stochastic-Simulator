package pec;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import optProblem.Event;
import optProblem.IEvent;

/**
 * @author grupo2 Generic Type PEC. Being generic of upper bound IEvent we can store any kind of
 *     classes that implement the Event interface in the PEC. The PEC will always be ordered so we
 *     need to provide the comparator when adding elements. The To String method was overridden.
 * @param <T> is the type that we want to use in the parameterized type
 */
public class PriorityQueuePec<T extends IEvent> implements IPec<T> {

    // ATTRIBUTES
    Queue<T> element_queue;
    int num_elements;

    // CONTRUCTORS
    /** Constructor for the PEC. It gets no arguments and will only initialize the array list. */
    public PriorityQueuePec(Comparator<T> c) {
        element_queue = new PriorityQueue<T>(c);
    }

    // METHODS
    /**
     * Getter method for the number of elements in the PEC. Since the PEC is in another package we
     * need the getter so the variables can remain package.
     *
     * @return is the number of elements in the PEC.
     */
    public int getNumElements() {
        return num_elements;
    }

    /**
     * Method to add a new element to the PEC. The element will be added and then the list will be
     * sorted.
     *
     * @param element is the element we wish to add to the PEC.
     * @param c is the comparator that we wish to use to sort our PEC.
     * @return is always true MIGHT NEED TO BE CHANGED DEPENDING ON
     *     ERRORS/EXCEPTIONS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public boolean addElement(T element, Comparator<T> c) {
        element_queue.add(element);
        num_elements++;
        return true;
    }

    /**
     * Getter method for the first element of the PEC. This method not only returns the first
     * element but also removes it from the PEC.
     *
     * @return is the first element in the PEC that invokes the method.
     */
    public T getFirstElement() {
        return element_queue.poll();
    }

    /**
     * Getter method for the whole element list. This allows us to iterate over it, for example.
     *
     * @return is the element list inside the PEC.
     */
    public Queue<T> getElementList() {
        return element_queue;
    }

    /** To String method that overrides the one with the same name in the Object superclass. */
    public String toString() {
        return "Pec: [" + element_queue + ", numEl=" + num_elements + "]";
    }

    
    public String toStringOrdered() {
        @SuppressWarnings("unchecked")
		List<Event> els = (List<Event>) new LinkedList<T>(element_queue);
        els.sort(Event.ec);

        return ("Pec: [" + els + ", numEl=" + num_elements + "]");
    }
}
