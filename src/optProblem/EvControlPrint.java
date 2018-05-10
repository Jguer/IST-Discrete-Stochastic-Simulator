package optProblem;

/**
 * Control Prints Event
 * 
 * @author group2
 *     <p>This subclass is an extension of the abstract Event class and will handle the Control
 *     Print events. It has no fields on its own but inherits time and individual from the
 *     superclass. Individual is set to null and time is set to a multiple of the total time divided
 *     by 20. It needs to override the Execute Event and toString methods.
 */
public class EvControlPrint extends Event {

    // ATTRIBUTES

    // CONTRUCTORS
    /**
     * This is the constructor. All it does is call the superclass' constructor with the arguments
     * provided. Individual is always null since it doesn't target any specific individual.
     *
     * @param time is the instant when we want to print the control information.
     */
    public EvControlPrint(double time) {
        super(time, null);
    }

    // METHODS
    /**
     * This method is a redefinition of the general Event method with the same name.
     *
     * @see optProblem.Event#ExecEvent(optProblem.OptProblem) for the default documentation. In this
     *     particular case we have to print the control information requested in the project paper.
     */
    public void ExecEvent(OptProblem opp) {

        StochasticOptProblem op = (StochasticOptProblem) opp;

        int num = op.getNumControlPrint();
        op.setNumControlPrint(op.getNumControlPrint() + 1);

        String format = "%-35s %-17d\n";
        String format2 = "%-35s %-26s\n";
        String format3 = "%-35s %-33s\n";
        String format4 = "%-35s %-16f\n";
        String format5 = "%-35s %d/%f\n";

        System.out.println("Observation " + num + ":");
        System.out.print("\t\t");
        System.out.printf(format4, "Present instant: ", op.getActual_time());
        System.out.print("\t\t");
        System.out.printf(format, "Number of realised events: ", op.getNum_events());
        System.out.print("\t\t");
        System.out.printf(format, "Population Size: ", op.getAliveIndividuals());
        if (op.isHit()) {
            System.out.print("\t\t");
            System.out.printf(format2, "Final point has been hit: ", "yes");
        } else {
            System.out.print("\t\t");
            System.out.printf(format2, "Final point has been hit: ", "no");
        }

        System.out.print("\t\t");
        System.out.printf(
                format3,
                "Path of the best fit individual: ",
                op.getBestIndividual().getHistory().toString());

        System.out.print("\t\t");
        System.out.printf(
                format5,
                "Cost/Comfort ",
                op.getBestIndividual().getCost(),
                op.getBestIndividual().getComfort());

        System.out.println();
        System.out.println();
    }

    /**
     * To String method that overrides the one with the same name in the Object superclass. Since
     * this class extends a superclass that is abstract and has this as a method then this
     * redefinition is mandatory.
     */
    public String toString() {
        return ("(Type:ControlPrint" + ",Time:" + this.getTime() + " & No ind)");
    }
}
