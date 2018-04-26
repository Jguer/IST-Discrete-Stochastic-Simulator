package optProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author antonio
 * This subclass is an extension of the abstract Event class and will handle the Reproduction events.
 * It has no fields on its own but inherits time and individual from the superclass.
 * It needs to specify the Execute Event and toString methods.
 */
public class EvRepr extends Event {
	
	
	//ATTRIBUTES

	//CONTRUCTORS
	/**
	 * This is the constructor. All it does is call the superclass' constructor with the arguments provided.
	 * @param timee is the time of the reproduction.
	 * @param ind is the Individual associated with this event.
	 */
	public EvRepr(double timee, Individual ind) {
		super(timee,ind);
	}
		
	//METHODS
	/**
	 * This method is a redefinition of the general Event method with the same name.
	 * @see optProblem.Event#ExecEvent(optProblem.OptProblem) for the default documentation.
	 * In this particular case we have 2 individuals involved. One of them is the parent and the other will be the son.
	 * For the parent we create a new reproduction and add it to the PEC if the time is lower than the death time of the individual.
	 * For the son we create the history based on the father's and then update its comfort and add the Death, Reproduction and Move events to the PEC.
	 */
	public void ExecEvent(OptProblem opp){
		
		StochasticOptProblem op = (StochasticOptProblem) opp;
		
		op.num_reprs++;
		
		//create "son" individual
		Individual ind = new Individual(op.total_inds++);
		op.alive_inds ++;
		op.list_inds.add(ind);
				
		//System.out.println("REPRODUCTION!!");
		//System.out.println(op.actual_time);
		Individual father = this.individual;
		
		
		//create new reproduction for the father - only add if happens before its death
		double randTime = op.actual_time + Event.expRandom(father.getValueForExpMean()*op.repr_mean);
		if(randTime<father.death_time) {
			op.pec.addElement(new EvRepr(randTime, father), ec);
		}
		
		if(father.history.size()>2) { //CHANGE THE VALUES IN THE PREFIX SIZE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			int father_hist_size = father.history.size();
			int prefix_size = (int) Math.floor(father_hist_size*(0.6+0.4*father.comfort));
			List<Point> newhist = Point.clonePointList(father.history);
			List<Integer> newcosts = new ArrayList<Integer>();
			newcosts.addAll(father.costs);
						
			newhist.subList(prefix_size+1, newhist.size()).clear();
			newcosts.subList(prefix_size+1, newcosts.size()).clear();
			ind.history = newhist;
			ind.costs = newcosts;
			
			//System.out.println("pre " + prefix_size + " histFather " + father_hist_size + " newhist " + newhist.size());
			
			int cycle_cost = 0;
			//check if i = 1 is correct!!! HERE WE MAY HAVE TO TAKE AWAY A COST BECAUSE OF THE FIRST POINT BEING THE START BUT I AM NOT SURE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			for(int i = 1;i<newcosts.size();i++) {
				cycle_cost += newcosts.get(i);
			}
			ind.cost = cycle_cost;
			
			if(Point.findSamePoint(newhist, op.goal) != -1)
				ind.hit = true;
			else
				ind.hit = false;
		
		}
		else {  //if size is 1 or 2 then we can do nothing but accept the starting point only i guess <---- CHECK!!!!!!!!!!!
			ind.history.add(op.start);		
			ind.cost = 0;
			ind.costs.add(1);
			ind.hit = false;
		}
		
		ind.updateComfort(op.goal,op.map.cmax, op.map.mapDimensions.x, op.map.mapDimensions.y, op.k);
		ind.death_time = op.actual_time + Event.expRandom(ind.getValueForExpMean()*op.death_mean);
		op.pec.addElement(new EvDeath(ind.death_time, ind), ec);
		
		
		// create move and only add if happens before death
		randTime = op.actual_time + Event.expRandom(ind.getValueForExpMean()*op.move_mean);
		if(randTime<ind.death_time) {
			op.pec.addElement(new EvMove(randTime, ind), ec);
		}
		
		//create reproduction and only add if happens before death
		randTime = op.actual_time + Event.expRandom(ind.getValueForExpMean()*op.repr_mean);
		if(randTime<ind.death_time) {
			op.pec.addElement(new EvRepr(randTime, ind), ec);
		}
		
		
	}

	
	/**
	 * To String method that overrides the one with the same name in the Object superclass.
	 * Since this class extends a superclass that is abstract and has this as a method then this redefinition is mandatory.
	 */
	public String toString() {
		return("(Type:Repr,Ind ID:" + this.individual.identifier + ",Time:" + this.time + ")");
	}

}
