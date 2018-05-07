package optProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class for Individuals. Each individual should have its own identifier, a death_time,
 * it's current comfort, the total cost of it's path so far and a boolean hit indicating if the
 * individual has reached the goal. An agent keeps two lists: - A history list to keep track of the
 * path it has covered so far; - A costs list, to be able to go back in the cost in case a loop is
 * detected; Equals, Hashcode and toString methods were overridden. There is an aditional "equals"
 * that can compare only the identifier of the indivifual. There are also methods for Individual
 * cloning and Individual List cloning.
 *
 * @author group2
 */
public class Individual {

    // ATTRIBUTES
    private final int identifier;
    private double death_time;
    private double comfort;
    private int cost;
    private boolean hit;
    private final List<Point> history;
    private final List<Integer> costs;

    public static IndividualComparator ic =
            new IndividualComparator(); // this can be used by any class that wants to compare 2
    // individuals, even outside our package

    // CONTRUCTORS
    /**
     * Constructor of the Individual object. It forces the user to provide an identifier. The lists
     * are initialized and the hit boolean is set to false.
     *
     * @param ident: identifier of the new individual. This value should be unique inside the same
     *     simulation.
     */
    public Individual(int ident) {
        identifier = ident;
        setHit(false);
        history = new ArrayList<Point>();
        costs = new ArrayList<Integer>();
    }

    /**
     * Constructor used to clone an Individual object. We are left with 2 equal individuals but
     * different in memory. This way we can keep changing the one in the simulation while keeping
     * the best saved. This method relies on the individual calling it being correctly initialized.
     *
     * @param clone: individual we want to clone.
     */
    public Individual(Individual clone) {
        identifier = clone.getIdentifier();
        setHit(false);
        setComfort(clone.getComfort());
        setDeathTime(clone.getDeathTime());
        setCost(clone.getCost());
        setHit(clone.isHit());
        history = new ArrayList<Point>(clone.getHistory());
        costs = new ArrayList<Integer>(clone.getCosts());
    }

    // METHODS

    /**
     * Method that will take care of updating the best individual. In case the goal has been hit
     * then the best is chosen by iterating over the list of individuals, only looking for the ones
     * that have hit the goal and choosing the one with the lowest cost. If the goal has not been
     * reached then we simply pick the individual with the highest comfort.
     *
     * @param list_inds: a list of individuals containing all the individuals currently alive in the
     *     simulation.
     * @param best: the best individual found so far during the simulation.
     * @param hit: a boolean indicating if any individual has found the goal during the current or
     *     not.
     * @return Returns the individual with the best path cost in list_inds.
     */
    public static Individual updateBest(List<Individual> list_inds, Individual best, boolean hit) {
        // if we already hit the goal at some point
        if (hit) {
            best = new Individual(Individual.getBestIndividual(list_inds, best));
        } else {
            for (Individual ind : list_inds) {
                // if we don't have a best yet OR this guy is better than the last
                if (best == null || ind.getComfort() > best.getComfort()) {
                    best = new Individual(ind);
                }
            }
        }

        return best;
    }

    /**
     * Method to find the individual with best path. This function should only be invoked after the
     * goal has been hit in the Optimization Problem. We will iterate over all the individuals we
     * have at the moment and, focusing only on the ones that have hit the goal, we will replace the
     * best if needed. In the end we will return the new best. In case there was no better
     * individual the same that we received will be sent.
     *
     * @param inds: list of alive individuals.
     * @param best: individual that has found the goal with lowest cost so far in the simulation.
     * @return Returns the new best individual that can be the same as we got or a new one.
     */
    public static Individual getBestIndividual(List<Individual> inds, Individual best) {
        for (Individual ind : inds) {
            if (ind.isHit() && (!best.isHit() || ind.getCost() < best.getCost())) {
                best = ind;
            }
        }
        return best;
    }

    /**
     * Method used to clone a list of Individuals. This method relies on the cloning constructor
     * being available.
     *
     * @param list: a list of Individuals that we want cloned.
     * @return Returns a list of Individuals that have same values of the original ones but are
     *     different objects in memory.
     */
    public static List<Individual> cloneIndividualList(List<Individual> list) {
        List<Individual> clone = new ArrayList<Individual>(list.size());
        for (Individual item : list) clone.add(new Individual(item));
        return clone;
    }

    /**
     * Method that looks for the Individual passed as argument in a list of Individuals. This method
     * is static meaning that it will have to be invoked from the class itself.
     *
     * @param inds is the list of individuals where we want to perform the search.
     * @param ind is the individual we wish to find in the list.
     * @return Returns the index of the individual in the list -1 if it isn't found.
     */
    public static int findSameIndividual(List<Individual> inds, Individual ind) {
        for (int i = 0; i < inds.size(); i++) {
            if (inds.get(i).equals(ind)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method that updates an individual's comfort. It should only be invoked by individuals that
     * have been initialized.
     *
     * @param goal: point of the map that we want to reach in the simulation.
     * @param map: Map used in simulation.
     * @param k: sensitivity parameter.
     */
    public void updateComfort(Point goal, Map map, int k) {

        Point current = getHistory().get(getHistory().size() - 1);

        double comf1 =
                (1.0
                        - (getCost() - getHistory().size() + 2.0)
                                / ((map.getCmax() - 1.0) * getHistory().size() + 3.0));
        int dist = Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());
        double comf2 =
                1.0
                        - ((dist)
                                / (map.getMapDimensions().getX()
                                        + map.getMapDimensions().getY()
                                        + 1.0));

        setComfort(Math.pow(comf1, k) * Math.pow(comf2, k));
    }

    /**
     * This method returns the value that we need to use to get the mean of the exponential pdf.
     * This value depends on the comfort of the individual and will need to be multiplied by the
     * event mean.
     *
     * @return Returns the value to be used in the exponential pdf.
     */
    public double getValueForExpMean() {
        return (1 - Math.log(1.0 - getComfort()));
    }

    /**
     * This method is similar to the redefinition of the equals but only takes into account the
     * identifier. It can be used to compare individuals that might have been cloned and can no
     * longer have the same history or cost.
     *
     * @param obj: the individual we want to compare with the one inkoving the method.
     * @return is true if they have the same identifier and false otherwise.
     */
    public boolean sameIdentifier(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Individual other = (Individual) obj;
        if (getIdentifier() != other.getIdentifier()) return false;
        return true;
    }

    /** To String method that overrides the one with the same name in the Object superclass. */
    @Override
    public String toString() {
        return "Individual[ID="
                + getIdentifier()
                + ",com="
                + getComfort()
                + ",cst="
                + getCost()
                + ",hit="
                + isHit()
                + ",hist="
                + getHistory()
                + "]";
    }

    /**
     * Hashcode method that overrides the one with the same name in the Object superclass. Was done
     * to complement the changes done to the equals method.
     *
     * @see java.lang.Object#hashCode() for the original JavaDocs.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(getComfort());
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + getCost();
        result = prime * result + ((getCosts() == null) ? 0 : getCosts().hashCode());
        temp = Double.doubleToLongBits(getDeathTime());
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((getHistory() == null) ? 0 : getHistory().hashCode());
        result = prime * result + (isHit() ? 1231 : 1237);
        result = prime * result + getIdentifier();
        return result;
    }

    /**
     * Equals method that overrides the one with the same name in the Object superclass.
     *
     * @param obj is the object to be compared with the one invoking the method.
     * @see java.lang.Object#equals(java.lang.Object) for the original JavaDocs.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Individual other = (Individual) obj;
        if (Double.doubleToLongBits(getComfort()) != Double.doubleToLongBits(other.getComfort()))
            return false;
        if (getCost() != other.getCost()) return false;
        if (getCosts() == null) {
            if (other.getCosts() != null) return false;
        } else if (!getCosts().equals(other.getCosts())) return false;
        if (Double.doubleToLongBits(getDeathTime())
                != Double.doubleToLongBits(other.getDeathTime())) return false;
        if (getHistory() == null) {
            if (other.getHistory() != null) return false;
        } else if (!getHistory().equals(other.getHistory())) return false;
        if (isHit() != other.isHit()) return false;
        if (getIdentifier() != other.getIdentifier()) return false;
        return true;
    }

    /** @return the identifier */
    public int getIdentifier() {
        return identifier;
    }

    /** @return the death_time */
    public double getDeathTime() {
        return death_time;
    }

    /** @param death_time the death_time to set */
    public void setDeathTime(double death_time) {
        this.death_time = death_time;
    }

    /** @return the comfort */
    public double getComfort() {
        return comfort;
    }

    /** @param comfort the comfort to set */
    public void setComfort(double comfort) {
        this.comfort = comfort;
    }

    /** @return the cost */
    public int getCost() {
        return cost;
    }

    /** @param cost the cost to set */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /** @return the hit */
    public boolean isHit() {
        return hit;
    }

    /** @param hit the hit to set */
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    /** @return the history */
    public List<Point> getHistory() {
        return history;
    }

    /** @return the costs */
    public List<Integer> getCosts() {
        return costs;
    }
}
