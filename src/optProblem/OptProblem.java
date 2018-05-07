package optProblem;

/**
 * @author group2
 * 
 * OptProblem is used in the <code>Main</code> and allows
 * us to give alternative implementations of optimization problems.
 *  {@see main.Main}
 *
 */
public interface OptProblem {

	/**
	 * This method should work as a constructor for an optimization problem, setting up everything that is needed for the simulate and run methods.
	 *
	 * Most of the parameters are self explanatory since they are the ones in the project paper.
	 *
	 * @param max_indss is the maximum number of individuals in the
	 * @param max_timee is the maximum simulation time
	 * @param dmean is the constant to multiply when getting the death mean for the exponential
	 * @param mmean is the constant to multiply when getting the move mean for the exponential
	 * @param rmean is the constant to multiply when getting the reproduction mean for the exponential
	 * @param m is the Map that we want to use in this optimization problem
	 * @param pi is the initial point where individuals should start
	 * @param pf is the final point that we want to achieve
	 * @param kk is the comfort sensitivity
	 * @param num_inds_init is the starting number of individuals
	 */
    void initialize(
            int max_indss,
            double max_timee,
            int dmean,
            int mmean,
            int rmean,
            Map m,
            Point pi,
            Point pf,
            int kk,
            int num_inds_init);

    void simulate();

    public void runOptimizationProblem(String filename);
}
