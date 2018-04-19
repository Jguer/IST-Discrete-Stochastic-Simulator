package optProblem;

public interface OptProblem {
	
	void initialize(int max_indss, double max_timee, int dmean, int mmean, int rmean, int xx, int yy, int no, int cmaxx, int kk, int num_inds_init);
	void simulate();

}
