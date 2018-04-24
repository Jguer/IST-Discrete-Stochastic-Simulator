package optProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author antonio
 * This is the class for Individuals. Each individual should have its own identifier .
 * Equals, Hashcode and toString were overridden.
 * There is an aditional "equals" that can compare ony the identifier.
 * There are also methods for Individual cloning and Individual List cloning.
 */
public class Individual {
	
	//ATTRIBUTES
	int identifier;
	double death_time;
	double comfort;
	int cost;
	boolean hit;
	List<Point> history;
	List<Integer> costs;
	

	//CONTRUCTORS
	/**
	 * Constructor of the Individual object. It forces the user to provide an identifier. The lists are initialized and the hit boolean is set to false.
	 * @param ident is the identifier of the current individual. This value should be unique inside the same simulation.
	 */
	public Individual(int ident) {
				
		identifier = ident;
		hit = false;
		history = new ArrayList<Point>();
		costs = new ArrayList<Integer>();
	}
		
	//constructor para fazer clone
	/**
	 * Constructor used to clone an Individual object. We are left with 2 equal individuals but different in memory. This way we can keep changing the one in the simulation while keeping the best saved.
	 * This method relies on the individual calling it being correctly initialized.
	 * @param clone is the individual we want to clone.
	 */
	public Individual(Individual clone) {
		this(clone.identifier);
		comfort = clone.comfort;
		death_time = clone.death_time;
		cost = clone.cost;
		hit = clone.hit;
		history = new ArrayList<Point>(clone.history);
		costs = new ArrayList<Integer>(clone.costs);
	}
	
	
	/**
	 * Method that will take care of updating the best individual.
	 * In case the goal has been hit than the best is chosen by iterating over the list of individuals, only looking for the ones that have hit the goal and choosing the one with the lowest cost. 
	 * If the goal has not been reached then we simply pick the individual with the highest comfort.
	 */
	public static Individual updateBest(List<Individual> list_inds, Individual best, boolean hit) {
		//if we already hit the goal at some point
		if(hit){
			best = new Individual(Individual.getBestIndividual(list_inds,best));
		}
		else {
			for(Individual ind: list_inds) {
				//if we don't have a best yet OR this guy is better than the last
				if(best == null || ind.comfort>best.comfort) {
					best = new Individual(ind);
				}
			}
		}
		
		return best;
	}	
	
	
				
	//METHODS
	
	//find individual with best path
	/**
	 * Method to find the individual with best path. This function should only be invoked after the goal has been hit in the Optimization Problem.
	 * We will iterate over all the individuals we have at the moment and, focusing only on the ones that have hit the goal, we will replace the best if needed.
	 * In the end we will return the new best. In case there was no better individual the same that we received will be sent.
	 * @param inds is the list of alive individuals.
	 * @param best is the current best individual.
	 * @return is the new best individual that can be the same as we got or a new one.
	 */
	public static Individual getBestIndividual(List<Individual> inds, Individual best){
		for(Individual ind: inds){
			if(ind.hit && (!best.hit || ind.cost<best.cost)) {
				best = ind;
			}
		}	
		return best;
	}
	
	/**
	 * Method using to clone a list of Individuals. This method relies on the cloning constructor being available.
	 * @param list is a list of Individuals that we want cloned.
	 * @return is the new list that contains Individuals with the same values of the original ones but are different objects in memory.
	 */
	public static List<Individual> cloneIndividualList(List<Individual> list) {
	    List<Individual> clone = new ArrayList<Individual>(list.size());
	    for (Individual item : list) clone.add(new Individual(item));
	    return clone;
	}	
	
	
	//returns the index of that same point in the array or -1 if it isn't there
	/**
	 * Method that looks for the Individual passed as argument in the List also passed as argument.
	 * This method is static meaning that it will have to be invoked from the class itself.
	 * @param inds is the list of individuals we want to perform the search in.
	 * @param ind is the individual we wish to find in the said list.
	 * @return is the index of that same point in the array or -1 if it isn't there.
	 */
	public static int findSameIndividual(List<Individual> inds, Individual ind) {
		for(int i=0;i<inds.size();i++) {
			if(inds.get(i).equals(ind)) {
				return i;
			}
		}
		return -1;
		
	}
	
	
	//method to update the individual's comfort
	/**
	 * Method that updates an individual's comfort. 
	 * It should only be invoked by individuals that have been initialized.
	 * @param goal is the final point that we want to reach in the simulation.
	 * @param cmax is the maximum cost of a Special Zone.
	 * @param n is the number of columns in the map.
	 * @param m is the number of rows in the map.
	 * @param k is the sensitivity parameter.
	 */
	public void updateComfort(Point goal,int cmax, int n, int m, int k){
		
		Point current = history.get(history.size()-1);
		
		double comf1 = (1.0-(cost-history.size()+2.0)/((cmax-1.0)*history.size()+3.0));
		int dist = Math.abs(current.x-goal.x) +Math.abs(current.y-goal.y);
		double comf2 = 1.0-((dist)/(n+m+1.0));
		
		comfort = Math.pow(comf1,k)*Math.pow(comf2,k);
		
	}
	
	/**
	 * This method returns the value that we need to use to get the mean of the exponential pdf.
	 * This value depends on the comfort of the individual and will need to be multiplied by the event mean.
	 * @return is the value of to be used in the exponential pdf.
 	 */
	public double getValueForExpMean() {
		return (1-Math.log(1.0-comfort));
	}
	
	
	/**
	 * This method is similar to the redefinition of the equals but only takes into account the identifier. It can be used to compare individuals that might have been cloned and can no longer have the same history or cost.
	 * @param obj should be the individual we want to compare with the one inkoving the method.
	 * @return is true if they have the same identifier and false otherwise.
	 */
	public boolean sameIdentifier(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Individual other = (Individual) obj;
		if (identifier != other.identifier)
			return false;
		return true;
	}
	
	/**
	 * To String method that overrides the one with the same name in the Object superclass.
	 */
	@Override
	public String toString() {
		return "Individual[ID=" + identifier + ",com=" + comfort + ",cst=" + cost + ",hit=" + hit
				+ ",hist=" + history + "]";
	}


	/**
	 * Hashcode method that overrides the one with the same name in the Object superclass. Was done to complement the changes done to the equals method.
	 * @see java.lang.Object#hashCode() for the original JavaDocs.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(comfort);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + cost;
		result = prime * result + ((costs == null) ? 0 : costs.hashCode());
		temp = Double.doubleToLongBits(death_time);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((history == null) ? 0 : history.hashCode());
		result = prime * result + (hit ? 1231 : 1237);
		result = prime * result + identifier;
		return result;
	}

	/**
	 * Equals method that overrides the one with the same name in the Object superclass.
	 * @param obj is the object to be compared with the one invoking the method.
	 * @see java.lang.Object#equals(java.lang.Object) for the original JavaDocs.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Individual other = (Individual) obj;
		if (Double.doubleToLongBits(comfort) != Double.doubleToLongBits(other.comfort))
			return false;
		if (cost != other.cost)
			return false;
		if (costs == null) {
			if (other.costs != null)
				return false;
		} else if (!costs.equals(other.costs))
			return false;
		if (Double.doubleToLongBits(death_time) != Double.doubleToLongBits(other.death_time))
			return false;
		if (history == null) {
			if (other.history != null)
				return false;
		} else if (!history.equals(other.history))
			return false;
		if (hit != other.hit)
			return false;
		if (identifier != other.identifier)
			return false;
		return true;
	}	
}
