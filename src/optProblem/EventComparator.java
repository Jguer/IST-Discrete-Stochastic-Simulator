package optProblem;

import java.util.Comparator;

/**
 * @author group2
 *     <p>Class that is just used to compare Events based on their time. When used with the
 *     list.sort(Comparator) method it will sort a List of Events by lowest time first. It
 *     implements the Comparator interface by overriding the compare method.
 */
public class EventComparator implements Comparator<Event> {

    /**
     * Method that overrides the comparator method. It receives two arguments and returns an integer
     * based on the comparison of the time field of the arguments.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object) for the original
     *     JavaDocs on the method.
     * @param e1 is the first event we want to compare.
     * @param e2 is the second event we want to compare.
     * @return is -1 if the first has lower time, +1 if its the other way around and 0 if it is the
     *     same.
     */
    public int compare(Event e1, Event e2) {

        double i1 = e1.time;
        double i2 = e2.time;

        return i1 < i2 ? -1 : i1 > i2 ? +1 : 0;
    }
}
