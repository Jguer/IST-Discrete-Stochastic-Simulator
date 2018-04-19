package optProblem;

public class Teste {

	
public static void main(String[] args) {
		
		OptProblem op = new StochasticOptProblem();
		op.initialize(50, 100, 10, 1, 1, 5, 4, 4, 4, 3, 10); //max_indss, max_timee,  dmean,  mmean,  rmean, x,  y, no, cmaxx, k, initial nº individuals)
		
		op.simulate();
		
	}
}
