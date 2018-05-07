package optProblem;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import parser.OptProblemHandler;
import pec.IPec;
import pec.PriorityQueuePec;

/**
 * Class of the Optimization Problem. We have 4 kinds of attributes that are non static: time
 * related, individual related, event related and map related.
 *
 * @version 1.0
 * @since 2018-04-30
 */
public class StochasticOptProblem implements OptProblem {

    // ATTRIBUTES

    // time related fields
    private double actual_time;
    private double max_time;

    // individual related fields
    private int alive_inds;
    private int total_inds;
    private int max_inds;
    private final List<Individual> list_inds;
    private Individual best;
    private int k;

    // event related fields
    private int num_events;
    private int num_deaths;
    private int num_moves;
    private int num_reprs;
    private int num_epidemics;
    private int num_ControlPrint = 1;
    private int death_mean;
    private int move_mean;
    private int repr_mean;
    private IPec<Event> pec;

    // map related fields
    private Map map;
    private Point start;
    private Point goal;
    private boolean hit;

    // CONTRUCTORS
    /**
     * Constructor with no arguments for the Optimization Problem. All it does is create the empty
     * lists that will be used.
     */
    public StochasticOptProblem() {
        setPec(new PriorityQueuePec<Event>(Event.ec));
        list_inds = new LinkedList<Individual>();
    }

    /**
     * More advanced constructor that actually has values to put in the variables. It invokes the no
     * arg constructor so we dont have to write the same thing twice.
     *
     * @param max_indss is the maximum number of alive individuals.
     * @param max_timee is the total simulation time.
     * @param dmean is the value to use in the mean of the death events.
     * @param mmean is the value to use in the mean of the move events.
     * @param rmean is the value to use in the mean of the reproduction events.
     * @param xx is the number of columns of the map (possible X values will go from 1 to xx
     *     inclusive).
     * @param yy is the number of rows of the map (possible Y values will go from 1 to yy
     *     inclusive).
     * @param no is the number of obstacles in the map.
     * @param cmaxx is the maximum cost of a Special Zone.
     * @param kk is the comfort sensitivity parameter.
     */
    public StochasticOptProblem(
            int max_indss,
            double max_timee,
            int dmean,
            int mmean,
            int rmean,
            int xx,
            int yy,
            int no,
            int cmaxx,
            int kk) {
        this();
        setActual_time(0);
        max_time = max_timee;
        max_inds = max_indss;
        setDeath_mean(dmean);
        setMove_mean(mmean);
        setRepr_mean(rmean);
        setMap(new Map(xx, yy, no, cmaxx));
        setK(kk);
        setHit(false);
    }

    // METHODS

    /**
     * Method to create the first set of individuals. Each call of this method creates one new
     * individual at the start point. The Death event is added to the PEC and the Move and
     * Reproduction are only added if they occur prior to the Death. Should only be called when the
     * Optimization Problem object has been correctly initialized using the full constructor.
     */
    public void createFirstInds() {

        // create individual

        Individual ind = new Individual(total_inds++);

        setAliveIndividuals(getAliveIndividuals() + 1);
        getIndividualsList().add(ind);

        ind.getHistory().add(getStart());
        ind.setCost(0);
        ind.getCosts().add(1);

        ind.updateComfort(getGoal(), getMap(), getK());
        ind.setDeathTime(
                getActual_time() + Event.expRandom(ind.getValueForExpMean() * getDeath_mean()));
        getPec().addElement(new EvDeath(ind.getDeathTime(), ind), Event.ec);

        // create move and only add if happens before death
        double randTime =
                getActual_time() + Event.expRandom(ind.getValueForExpMean() * getMove_mean());
        if (randTime < ind.getDeathTime()) {
            getPec().addElement(new EvMove(randTime, ind), Event.ec);
        }

        // create reproduction and only add if happens before death
        randTime = getActual_time() + Event.expRandom(ind.getValueForExpMean() * getRepr_mean());
        if (randTime < ind.getDeathTime()) {
            getPec().addElement(new EvRepr(randTime, ind), Event.ec);
        }
    }

    /* (non-Javadoc)
     * @see optProblem.OptProblem#initialize(int, double, int, int, int, optProblem.Map, optProblem.Point, optProblem.Point, int, int)
     */
    public void initialize(
            int max_indss,
            double max_timee,
            int dmean,
            int mmean,
            int rmean,
            Map parsedMap,
            Point initialPoint,
            Point finalPoint,
            int kk,
            int num_inds_init) {

        setActual_time(0);
        max_time = max_timee;
        max_inds = max_indss;
        setDeath_mean(dmean);
        setMove_mean(mmean);
        setRepr_mean(rmean);
        setMap(parsedMap);
        setK(kk);
        setHit(false);

        int num_ctrl = 20;
        double ctrl_time = (double) max_time / num_ctrl;

        setStart(initialPoint);
        setGoal(finalPoint);

        for (int i = 0;
                i < num_inds_init;
                i++) { // creates the first individuals at the starting point
            this.createFirstInds();
        }
        this.setBestIndividual(
                Individual.updateBest(
                        getIndividualsList(),
                        getBestIndividual(),
                        isHit())); // updates the best one so far

        for (int j = 1; j <= num_ctrl; j++) { // adding the Control Print events to the PEC
            getPec().addElement(new EvControlPrint(ctrl_time * j), Event.ec);
        }

        // System.out.println(op.pec.toString());

    }

    /* (non-Javadoc)
     * @see optProblem.OptProblem#simulate()
     */
    public void simulate() {

        Event ev;

        System.out.println("Pec: " + this.getPec().toStringOrdered());

        // ================= SIMULATING =============================
        while (this.getAliveIndividuals() > 0 && this.getActual_time() < this.max_time) {

            ev = this.getPec().getFirstElement(); // get next event from PEC
            // System.out.println("ev: " +ev);
            this.setActual_time(ev.getTime()); // fast forward until its time to execute it
            this.setNum_events(this.getNum_events() + 1);
            ev.ExecEvent(this);

            if (ev.getIndividual() != null) { // if it was an event with an individual associated
                ev.getIndividual().updateComfort(this.getGoal(), this.getMap(), this.getK());
                this.setBestIndividual(
                        Individual.updateBest(
                                getIndividualsList(),
                                getBestIndividual(),
                                isHit())); // updates the best one so far

                if (this.getAliveIndividuals()
                        > this.max_inds) { // launch an epidemic - it will have time = current
                    // time so we will
                    // execute it right away
                    this.getPec().addElement(new EvEpidemic(this), Event.ec);
                }
            }
        }

        // ======================= WE'RE OUT OF THE SIMULATION!!! ===============================
        System.out.println(
                "Path of the best fit individual: "
                        + this.getBestIndividual().getHistory().toString());
    }

    /* (non-Javadoc)
     * @see optProblem.OptProblem#runOptimizationProblem(java.lang.String)
     */
    public void runOptimizationProblem(String filename) {

        // max_indss, max_timee,  dmean,  mmean,  rmean, x,  y, no, cmaxx, k, initial nº
        // individuals){

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            OptProblemHandler myhandler = new OptProblemHandler();
            File inputFile = new File(filename);
            saxParser.parse(inputFile, myhandler);

            this.initialize(
                    myhandler.getMaxpop(),
                    myhandler.getFinalinst(),
                    myhandler.getDmean(),
                    myhandler.getMmean(),
                    myhandler.getRmean(),
                    myhandler.getMap(),
                    myhandler.getInitialPoint(),
                    myhandler.getFinalPoint(),
                    myhandler.getComfortsens(),
                    myhandler.getInitpop());

            this.simulate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** @return the actual_time */
    public double getActual_time() {
        return actual_time;
    }

    /** @param actual_time the actual_time to set */
    public void setActual_time(double actual_time) {
        this.actual_time = actual_time;
    }

    /** @return the list_inds */
    public List<Individual> getIndividualsList() {
        return list_inds;
    }

    /** @return the alive_inds */
    public int getAliveIndividuals() {
        return alive_inds;
    }

    /** @param alive_inds the alive_inds to set */
    public void setAliveIndividuals(int alive_inds) {
        this.alive_inds = alive_inds;
    }

    /** @return the best */
    public Individual getBestIndividual() {
        return best;
    }

    /** @param best the best to set */
    public void setBestIndividual(Individual best) {
        this.best = best;
    }

    /** @return the k */
    public int getK() {
        return k;
    }

    /** @param k the k to set */
    public void setK(int k) {
        this.k = k;
    }

    /** @return the total_inds */
    public int getTotalIndividuals() {
        return total_inds;
    }

    /** @param total_inds the total_inds to set */
    public void setTotalIndividuals(int total_inds) {
        this.total_inds = total_inds;
    }

    /** @return the num_ControlPrint */
    public int getNumControlPrint() {
        return num_ControlPrint;
    }

    /** @param num_ControlPrint the num_ControlPrint to set */
    public void setNumControlPrint(int num_ControlPrint) {
        this.num_ControlPrint = num_ControlPrint;
    }

    /** @return the num_deaths */
    public int getNum_deaths() {
        return num_deaths;
    }

    /** @param num_deaths the num_deaths to set */
    public void setNum_deaths(int num_deaths) {
        this.num_deaths = num_deaths;
    }

    /** @return the num_epidemics */
    public int getNum_epidemics() {
        return num_epidemics;
    }

    /** @param num_epidemics the num_epidemics to set */
    public void setNum_epidemics(int num_epidemics) {
        this.num_epidemics = num_epidemics;
    }

    /** @return the num_moves */
    public int getNum_moves() {
        return num_moves;
    }

    /** @param num_moves the num_moves to set */
    public void setNum_moves(int num_moves) {
        this.num_moves = num_moves;
    }

    /** @return the move_mean */
    public int getMove_mean() {
        return move_mean;
    }

    /** @param move_mean the move_mean to set */
    public void setMove_mean(int move_mean) {
        this.move_mean = move_mean;
    }

    /** @return the num_reprs */
    public int getNum_reprs() {
        return num_reprs;
    }

    /** @param num_reprs the num_reprs to set */
    public void setNum_reprs(int num_reprs) {
        this.num_reprs = num_reprs;
    }

    /** @return the repr_mean */
    public int getRepr_mean() {
        return repr_mean;
    }

    /** @param repr_mean the repr_mean to set */
    public void setRepr_mean(int repr_mean) {
        this.repr_mean = repr_mean;
    }

    /** @return the death_mean */
    public int getDeath_mean() {
        return death_mean;
    }

    /** @param death_mean the death_mean to set */
    public void setDeath_mean(int death_mean) {
        this.death_mean = death_mean;
    }

    /** @return the num_events */
    public int getNum_events() {
        return num_events;
    }

    /** @param num_events the num_events to set */
    public void setNum_events(int num_events) {
        this.num_events = num_events;
    }

    /** @return the pec */
    public IPec<Event> getPec() {
        return pec;
    }

    /** @param pec the pec to set */
    public void setPec(IPec<Event> pec) {
        this.pec = pec;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    /** @return the goal */
    public Point getGoal() {
        return goal;
    }

    /** @param goal the goal to set */
    public void setGoal(Point goal) {
        this.goal = goal;
    }

    /** @return the hit */
    public boolean isHit() {
        return hit;
    }

    /** @param hit the hit to set */
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    /** @return the start */
    public Point getStart() {
        return start;
    }

    /** @param start the start to set */
    public void setStart(Point start) {
        this.start = start;
    }
}
	
	

/**
   * Main method to run an optimization problem. The flow is the following: 1) Create the starting
   * set of individuals and add their events to the PEC 2) In a cycle, remove the first event in the
   * PEC and execute it 3) During 2) always check if there is a need for an epidemic. 4) The cycle
   * ends when there are no more alive individuals or the simulation time is over.
   *
   * @param args are the arguments passed to the main function when executing the program.
   */
