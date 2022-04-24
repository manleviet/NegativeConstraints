package at.tugraz.ist.ase.negative;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.impl.BoolVarImpl;

/**
 * Test new negative alternative relationship.
 */
public class Main5 {
    static Model model;
    static BoolVar Smartwatch;
    static BoolVar Connector;
    static BoolVar Screen;
    static BoolVar Camera;
    static BoolVar Compass;
    static BoolVar GPS;
    static BoolVar Cellular;
    static BoolVar Wifi;
    static BoolVar Bluetooth;
    static BoolVar Analog;
    static BoolVar HighResolution;
    static BoolVar E_ink;

    public static void main(String[] args) {
        model = new Model();
        Smartwatch = model.boolVar("Smartwatch");
        Connector = model.boolVar("Connector");
        Screen = model.boolVar("Screen");
        Camera = model.boolVar("Camera");
        Compass = model.boolVar("Compass");
        GPS = model.boolVar("GPS");
        Cellular = model.boolVar("Cellular");
        Wifi = model.boolVar("Wifi");
        Bluetooth = model.boolVar("Bluetooth");
        Analog = model.boolVar("Analog");
        HighResolution = model.boolVar("HighResolution");
        E_ink = model.boolVar("E_ink");

        int startIdx = 0;
//        // Alternative
//        // [Analog <-> (not(HighResolution) /\ not(E_ink) /\ Screen)] /\
//        // [HighResolution <-> (not(Analog) /\ not(E_ink) /\ Screen)] /\
//        // [E_ink <-> (not(Analog) /\ not(HighResolution) /\ Screen)]

        BoolVar[] vars = new BoolVar[] {Analog, HighResolution, E_ink};
        BoolVar boolVar = model.boolVar();
        BoolVar equalVar = model.boolVar();
        model.sum(vars, "!=", 1).reifyWith(boolVar);
        model.sum(vars, "=", 1).reifyWith(equalVar);

        LogOp logOp = LogOp.or(LogOp.and(Screen.not(), Analog.not(), HighResolution.not(), E_ink.not()),
                LogOp.and(Screen, equalVar));
        LogOp negLopOp = LogOp.or(LogOp.and(Screen.not(), LogOp.or(Analog, HighResolution, E_ink)),
                LogOp.and(Screen, boolVar));

        model.addClauses(logOp);
        print("alternative(Screen, Analog, HighResolution, E_ink)", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(alternative(Screen, Analog, HighResolution, E_ink))", startIdx);

        // true - false
//        model.addClauseTrue(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseTrue(E_ink);
//        // true - false
//        model.addClauseTrue(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseFalse(E_ink);
//        // true - false
//        model.addClauseTrue(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseFalse(E_ink);
//        // true - false
//        model.addClauseFalse(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseFalse(E_ink);
//
//        // false - true
//        model.addClauseTrue(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseFalse(E_ink);
//        // false - true - no solution
//        model.addClauseTrue(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseTrue(E_ink);
//        // false - true - no solution
//        model.addClauseTrue(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseTrue(E_ink);
//        // false - true - no solution
//        model.addClauseTrue(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseFalse(E_ink);
//        // false - true
//        model.addClauseTrue(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseTrue(E_ink);
//        // false - true
//        model.addClauseFalse(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseTrue(E_ink);
//        // false - true
//        model.addClauseFalse(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseFalse(E_ink);
//        // false - true
//        model.addClauseFalse(Screen);
//        model.addClauseFalse(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseTrue(E_ink);
//        // false - true
//        model.addClauseFalse(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseFalse(E_ink);
//        // false - true
//        model.addClauseFalse(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseFalse(HighResolution);
//        model.addClauseTrue(E_ink);
//        // false - true
//        model.addClauseFalse(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseFalse(E_ink);
//        // false - true
//        model.addClauseFalse(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseTrue(E_ink);

        solve();
    }

    private static void solve() {
        if (model.getSolver().solve()) {
            System.out.println("Smartwatch = " + Smartwatch.getValue());
            System.out.println("Connector = " + Connector.getValue());
            System.out.println("Screen = " + Screen.getValue());
            System.out.println("Camera = " + Camera.getValue());
            System.out.println("Compass = " + Compass.getValue());
            System.out.println("GPS = " + GPS.getValue());
            System.out.println("Cellular = " + Cellular.getValue());
            System.out.println("Wifi = " + Wifi.getValue());
            System.out.println("Bluetooth = " + Bluetooth.getValue());
            System.out.println("Analog = " + Analog.getValue());
            System.out.println("HighResolution = " + HighResolution.getValue());
            System.out.println("E_ink = " + E_ink.getValue());
        } else {
            System.out.println("No solution");
        }
    }

    private static void print(String constraint, int startIdx) {
        System.out.println(constraint);
        int index = startIdx;
        while (index < model.getNbCstrs()) {
            System.out.println(model.getCstrs()[index]);
            index++;
        }
        System.out.println("-----------------");
    }
}
