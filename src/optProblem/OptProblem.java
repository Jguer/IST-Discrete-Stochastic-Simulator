package optProblem;

public interface OptProblem {
	
	void initialize(int max_indss, double max_timee, int dmean, int mmean, int rmean, Map m, Point pi, Point pf, int kk, int num_inds_init);
	void simulate();
	public void runOptimizationProblem(String filename);

}
