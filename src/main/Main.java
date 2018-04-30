package main;

import optProblem.*;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(
                    "Error - You must give the name of the file and place it in the directory!");
            System.exit(-1);
        }
        String filename = args[0];

        OptProblem op = new StochasticOptProblem();

        op.runOptimizationProblem(filename);
    }

}
