package pec;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import optProblem.IEvent;

/**
 * @author antonio
 * Generic Type PEC. Being generic of upperbound IEvent we can store any kind of classes that implement the Event interface in the PEC.
 * The PEC will always be ordered so we need to provide the comparator when adding elements.
 * The To String method was overridden.
 * @param <T> is the type that we want to use in the parameterized type 
 */
public class LinkedListPec<T extends IEvent> implements IPec<T>{
	
	//ATTRIBUTES
	List <T> element_list;
	int num_elements;
	

	//CONTRUCTORS
	/**
	 * Constructor for the PEC. It gets no arguments and will only initialize the array list.
	 */
	public LinkedListPec() {
		element_list = new LinkedList<T>();
	}
		
		
	//METHODS
	/**
	 * Getter method for the number of elements in the PEC.
	 * Since the PEC is in another package we need the getter so the variables can remain package.
	 * @return is the number of elements in the PEC.
	 */
	public int getNumElements() {
		return num_elements;
	}
	
	/**
	 * Method to add a new element to the PEC. The element will be added and then the list will be sorted.
	 * @param element is the element we wish to add to the PEC.
	 * @param c is the comparator that we wish to use to sort our PEC.
	 * @return is always true MIGHT NEED TO BE CHANGED DEPENDING ON ERRORS/EXCEPTIONS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	public boolean addElement(T element, Comparator<T> c){
		element_list.add(element);
		element_list.sort(c);
		num_elements++;
		return true;
	}
	
	/**
	 * Getter method for the first element of the PEC.
	 * This method not only returns the first element but also removes it from the PEC.
	 * @return is the first element in the PEC that invokes the method.
	 */
	public T getFirstElement(){
		if (num_elements > 0) {
			num_elements--;
			return(element_list.remove(0));
		}
		System.exit(-1);
		return(element_list.remove(0)); //so it doesn't give a compile time error for not returning
	}
	
	
	
	/**
	 * Getter method for any element in the PEC.
	 * This method does not remove the said element from the PEC.
	 * @param i is the index of the element we want to retrieve.
	 * @return is the element that was inside the said index of the list.
	 */
	public T getElementByIndex(int i){
		
		if(i<0 || i>element_list.size()-1)  //if its an impossible index
			System.exit(-1);
		
		
		return(element_list.get(i));
	}
	
	
	/**
	 * Getter method for the whole element list. This allows us to iterate over it, for example.
	 * @return is the element list inside the PEC.
	 */
	public List<T> getElementList(){
		return element_list;
	}

	/**
	 * To String method that overrides the one with the same name in the Object superclass.
	 */
	public String toString() {
		return "Pec: [" + element_list + ", numEl=" + num_elements + "]";
	}


	@Override
	public String toStringOrdered() {
		return this.toString();
	}
	
	

}
