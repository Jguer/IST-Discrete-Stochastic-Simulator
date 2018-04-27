package optProblem;

import java.util.Comparator;

/**
 * @author antonio Class that is just used to compare between individuals When used with the
 *     list.sort(Comparator) method it will sort a List of individuals by highest comfort first. It
 *     implements the generic Comparator interface by overriding the compare method.
 */
public class IndividualComparator implements Comparator<Individual> {
    /**
     * Method that overrides the comparator method. It receives two arguments and returns an integer
     * based on the comparation of the comfort field of the arguments.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object) for the original
     *     JavaDocs on the method.
     * @param i1 is the first individual we want to compare.
     * @param i2 is the second individual we want to compare.
     * @return is -1 if the first has higher comfort, +1 if its the other way around and 0 if it is
     *     the same.
     */
    public int compare(Individual i1, Individual i2) {

        double c1 = i1.comfort;
        double c2 = i2.comfort;

        return c1 > c2 ? -1 : c1 < c2 ? +1 : 0;
    }
}
