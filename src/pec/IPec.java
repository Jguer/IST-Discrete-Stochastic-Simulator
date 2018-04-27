package pec;

import java.util.Collection;
import java.util.Comparator;

public interface IPec<T> {

    // METHODS
    /**
     * Getter method for the number of elements in the PEC. Since the PEC is in another package we
     * need the getter so the variables can remain package.
     *
     * @return is the number of elements in the PEC.
     */
    public int getNumElements();

    /**
     * Method to add a new element to the PEC. The element will be added and then the list will be
     * sorted.
     *
     * @param element is the element we wish to add to the PEC.
     * @param c is the comparator that we wish to use to sort our PEC.
     * @return is always true MIGHT NEED TO BE CHANGED DEPENDING ON
     *     ERRORS/EXCEPTIONS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public boolean addElement(T element, Comparator<T> c);

    /**
     * Getter method for the first element of the PEC. This method not only returns the first
     * element but also removes it from the PEC.
     *
     * @return is the first element in the PEC that invokes the method.
     */
    public T getFirstElement();

    /**
     * Getter method for the whole element list. This allows us to iterate over it, for example.
     *
     * @return is the element list inside the PEC.
     */
    public Collection<T> getElementList();

    /** To String method that overrides the one with the same name in the Object superclass. */
    public String toString();

    public String toStringOrdered();
}
