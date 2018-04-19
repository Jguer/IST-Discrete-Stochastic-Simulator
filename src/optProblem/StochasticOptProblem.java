package optProblem;


import java.util.LinkedList;
import java.util.List;

import pec.Pec;

/**
 * @author antonio
 * Class of the Optimization Problem. We have 4 kinds of attributes that are non static:
 * time related, individual related, event related and map related.
 * There is a static field containing the EventCompartor. This can be the same for all instances of Optimization Problems.
 */
public class StochasticOptProblem implements OptProblem{
	
	//ATTRIBUTES
	
	//time related fields
	double actual_time;
	double max_time;
	
	//individual related fields
	int alive_inds;
	int total_inds;
	int max_inds;
	List<Individual> list_inds;
	Individual best;
	int k;
	
	//event related fields
	int num_events;
	int num_deaths;
	int num_moves;
	int num_reprs;
	int num_epidemics;
	int num_ControlPrint = 1;
	int death_mean;
	int move_mean;
	int repr_mean;
	Pec<Event> pec;
	
	//map related fields
	Map map;
	Point start;
	Point goal;
	boolean hit;
	
	
	static EventComparator ec = new EventComparator();
	
	//CONTRUCTORS
	/**
	 * Constructor with no arguments for the Optimization Problem. All it does is create the empty lists that will be used.
	 */
	public StochasticOptProblem() {
		pec = new Pec<Event>();
		list_inds = new LinkedList<Individual>();
	}
	
	/**
	 * More advanced constructor that actually has values to put in the variables.
	 * It invokes the no arg constructor so we dont have to write the same thing twice.
	 * @param max_indss is the maximum number of alive individuals.
	 * @param max_timee is the total simulation time.
	 * @param dmean is the value to use in the mean of the death events.
	 * @param mmean is the value to use in the mean of the move events.
	 * @param rmean is the value to use in the mean of the reproduction events.
	 * @param xx is the number of columns of the map (possible X values will go from 1 to xx inclusive).
	 * @param yy is the number of rows of the map (possible Y values will go from 1 to yy inclusive).
	 * @param no is the number of obstacles in the map.
	 * @param cmaxx is the maximum cost of a Special Zone.
	 * @param kk is the comfort sensitivity parameter.
	 */
	public StochasticOptProblem(int max_indss, double max_timee, int dmean, int mmean, int rmean, int xx, int yy, int no, int cmaxx, int kk){
		this();
		actual_time = 0;
		max_time = max_timee;
		max_inds = max_indss;
		death_mean = dmean;
		move_mean = mmean;
		repr_mean = rmean;
		map = new Map(xx,yy,no,cmaxx);
		k = kk;
		hit = false;
	}
	
	//METHODS
	
	//talvez depois por no individual??????????????????????????????????????????????????????
	/**
	 * Method that will take care of updating the best individual.
	 * In case the goal has been hit than the best is chosen by iterating over the list of individuals, only looking for the ones that have hit the goal and choosing the one with the lowest cost. 
	 * If the goal has not been reached then we simply pick the individual with the highest comfort.
	 */
	public void updateBest() {
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
	}		

	
	/**
	 * Method to create the first set of individuals. Each call of this method creates one new individual at the start point.
	 * The Death event is added to the PEC and the Move and Reproduction are only added if they occur prior to the Death.
	 * Should only be called when the Optimization Problem object has been correctly initialized using the full constructor.
	 */
	public void createFirstInds(){
		
		//create individual
		
		Individual ind = new Individual(total_inds++);
		
		alive_inds ++;
		list_inds.add(ind);
	
		ind.history.add(start);		
		ind.cost = 0;
		ind.costs.add(1);
		
		ind.updateComfort(goal,map.cmax, map.mapDimensions.x, map.mapDimensions.y, k);
		ind.death_time = actual_time + Event.expRandom(ind.getValueForExpMean()*death_mean);
		pec.addElement(new EvDeath(ind.death_time, ind), ec);
		
		// create move and only add if happens before death
		double randTime = actual_time + Event.expRandom(ind.getValueForExpMean()*move_mean);
		if(randTime<ind.death_time) {
			pec.addElement(new EvMove(randTime, ind), ec);
		}
		
		//create reproduction and only add if happens before death
		randTime = actual_time + Event.expRandom(ind.getValueForExpMean()*repr_mean);
		if(randTime<ind.death_time) {
			pec.addElement(new EvRepr(randTime, ind), ec);
		}
		
	}	

	
	public void initialize(int max_indss, double max_timee, int dmean, int mmean, int rmean, int xx, int yy, int no, int cmaxx, int kk, int num_inds_init){
		
		
		actual_time = 0;
		max_time = max_timee;
		max_inds = max_indss;
		death_mean = dmean;
		move_mean = mmean;
		repr_mean = rmean;
		map = new Map(xx,yy,no,cmaxx);
		k = kk;
		hit = false;
		
		int num_ctrl = 20;
		double ctrl_time = (double)max_time/num_ctrl;
		
		start = new Point(1,1);
		goal = new Point(5,4);
		map.obstacles[0] = new Point(2,1);  //depois pode ser uma funcao dentro do map tipo addObstacle() na interface e depois cada map sabe como é que faz
		map.obstacles[1] = new Point(2,3);
		map.obstacles[2] = new Point(2,4);
		map.obstacles[3] = new Point(4,2);
		map.specialZones.add(new SpecialZone(new Point(2,2),new Point(3,3),4));
		

		for(int i=0; i<num_inds_init; i++) { //creates the first individuals at the starting point
			this.createFirstInds(); 
		}
		this.updateBest(); //updates the best one so far
		
		for(int j=1;j<=num_ctrl;j++) { //adding the Control Print events to the PEC
			pec.addElement(new EvControlPrint(ctrl_time*j), ec);
		}
		
		//System.out.println(op.pec.toString());

	}
	
	public void simulate(){
		
		Event ev;
						
		// ================= SIMULATING =============================
		while(this.alive_inds > 0 && this.actual_time < this.max_time) {

			
			ev = this.pec.getFirstElement(); //get next event from PEC
			//System.out.println("ev: " +ev);
			this.actual_time = ev.time; //fast forward until its time to execute it
			this.num_events++;
			ev.ExecEvent(this);
			
			if(ev.individual!= null) { //if it was an event with an individual associated
				ev.individual.updateComfort(this.goal, this.map.cmax,  this.map.mapDimensions.x,  this.map.mapDimensions.y, this.k);
				this.updateBest(); //update the best individual since we might have reached the goal or have a new best comfort
			
				if(this.alive_inds>this.max_inds) {//launch an epidemic - it will have time = current time so we will execute it right away
					this.pec.addElement(new EvEpidemic(this), ec);
				}
			}
		}
		
		// ======================= WE'RE OUT OF THE SIMULATION!!! ===============================
		System.out.println("\n\n");
		if(this.actual_time>=this.max_time)
			System.out.println("Simulation ended because of the TIME LIMIT.");
		else if(this.alive_inds <= 0)
			System.out.println("Simulation ended because WE HAD NO MORE INDIVIDUALS");
		
		System.out.println("Total nº Events: " + this.num_events);
		System.out.println("Total nº Repr: " + this.num_reprs);
		System.out.println("Total nº Move: " + this.num_moves);
		System.out.println("Total nº Death: " + this.num_deaths);
		System.out.println("Total nº Epidemics: " + this.num_epidemics);
		
		System.out.println("Hit the goal? " + this.hit);
		System.out.println("Path of the best fit individual: " +  this.best.history.toString());
		System.out.println("Cost: " + this.best.cost);
		System.out.println("Comfort: " + this.best.comfort);
		
		
	}
	
	public static void main(String[] args) {
		
		//max_indss, max_timee,  dmean,  mmean,  rmean, x,  y, no, cmaxx, k, initial nº individuals){
		OptProblem op = new StochasticOptProblem();
		op.initialize(50, 100, 10, 1, 1, 5, 4, 4, 4, 3, 10);
		
		op.simulate();
		
	}
	
}
	
	
	// ------------------------------------ MAIN METHOD --------------------------------------------------------
	/**
	 * Main method to run an optimization problem.
	 * The flow is the following:
	 * 1) Create the starting set of individuals and add their events to the PEC
	 * 2) In a cycle, remove the first event in the PEC and execute it
	 * 3) During 2) always check if there is a need for an epidemic.
	 * 4) The cycle ends when there are no more alive individuals or the simulation time is over.
	 * @param args are the arguments passed to the main function when executing the program.
	 */
/*	public static void main(String[] args) {
		
		//INITIALIZATION =========================
		//max_indss, max_timee,  dmean,  mmean,  rmean, x,  y, no, cmaxx, k){
		OptProblem op = new OptProblem(50, 100, 10, 1, 1, 5, 4, 4, 4, 3);
		
		int num_inds_init = 10;
		int num_ctrl = 20;
		double ctrl_time = (double)op.max_time/num_ctrl;
		Event ev;
		
		op.start = new Point(1,1);
		op.goal = new Point(5,4);
		op.obstacles[0] = new Point(2,1);
		op.obstacles[1] = new Point(2,3);
		op.obstacles[2] = new Point(2,4);
		op.obstacles[3] = new Point(4,2);
		op.specialZones.add(new SpecialZone(new Point(2,2),new Point(3,3),4));
		

		for(int i=0; i<num_inds_init; i++) { //creates the first individuals at the starting point
			op.createFirstInds(); 
		}
		op.updateBest(); //updates the best one so far
		
		for(int j=1;j<=num_ctrl;j++) { //adding the Control Print events to the PEC
			op.pec.addElement(new EvControlPrint(ctrl_time*j), ec);
		}
		
		//System.out.println(op.pec.toString());
						
		// ================= SIMULATING =============================
		while(op.alive_inds > 0 && op.actual_time < op.max_time) {

			
			ev = op.pec.getFirstElement(); //get next event from PEC
			//System.out.println("ev: " +ev);
			op.actual_time = ev.time; //fast forward until its time to execute it
			op.num_events++;
			ev.ExecEvent(op);
			
			if(ev.individual!= null) { //if it was an event with an individual associated
				ev.individual.updateComfort(op.goal, op.cmax,  op.mapDimensions.x,  op.mapDimensions.y, op.k);
				op.updateBest(); //update the best individual since we might have reached the goal or have a new best comfort
			
				if(op.alive_inds>op.max_inds) {//launch an epidemic - it will have time = current time so we will execute it right away
					op.pec.addElement(new EvEpidemic(op), ec);
				}
			}
		}
		
		// ======================= WE'RE OUT OF THE SIMULATION!!! ===============================
		System.out.println("\n\n");
		if(op.actual_time>=op.max_time)
			System.out.println("Simulation ended because of the TIME LIMIT.");
		else if(op.alive_inds <= 0)
			System.out.println("Simulation ended because WE HAD NO MORE INDIVIDUALS");
		
		System.out.println("Total nº Events: " + op.num_events);
		System.out.println("Total nº Repr: " + op.num_reprs);
		System.out.println("Total nº Move: " + op.num_moves);
		System.out.println("Total nº Death: " + op.num_deaths);
		System.out.println("Total nº Epidemics: " + op.num_epidemics);
		
		System.out.println("Hit the goal? " + op.hit);
		System.out.println("Path of the best fit individual: " +  op.best.history.toString());
		System.out.println("Cost: " + op.best.cost);
		System.out.println("Comfort: " + op.best.comfort);
		
		
	}
}*/
