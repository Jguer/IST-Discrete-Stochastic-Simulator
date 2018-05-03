package main;

import optProblem.*;

/**
 * Main class used to test the program
 * 
 * @author grupo2
 *
 */
public class Main {

	/**
	 * Main method used to test the program
	 * There is a need for one and only one argument passed when running the code.
	 * 
	 * @author grupo2
	 *
	 * @param name of the test file used 
	 */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(
                    "Error - You must give the name of the file and place it in the directory!");
            System.exit(-1);
        }
        String filename = args[0];
        // Creating a new optimization problem
        OptProblem op = new StochasticOptProblem();
        // Running the said optimization problem. The parsing, initializing and running will all be done inside this function.
        op.runOptimizationProblem(filename);
    }
}