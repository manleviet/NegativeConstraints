package at.tugraz.ist.ase.negative;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;

import java.util.ArrayList;
import java.util.List;

/**
 * Test context constraints.
 */
public class
Main6 {
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

    static BoolVar KB;

    public static void main(String[] args) {
        model = new Model();
//        Smartwatch = model.boolVar("Smartwatch");
//        Connector = model.boolVar("Connector");
        Screen = model.boolVar("Screen");
//        Camera = model.boolVar("Camera");
//        Compass = model.boolVar("Compass");
//        GPS = model.boolVar("GPS");
//        Cellular = model.boolVar("Cellular");
//        Wifi = model.boolVar("Wifi");
//        Bluetooth = model.boolVar("Bluetooth");
        Analog = model.boolVar("Analog");
        HighResolution = model.boolVar("HighResolution");
        E_ink = model.boolVar("E_ink");

        KB = model.boolVar("KB");

        int startIdx = 0;
//        // Alternative
//        // [Analog <-> (not(HighResolution) /\ not(E_ink) /\ Screen)] /\
//        // [HighResolution <-> (not(Analog) /\ not(E_ink) /\ Screen)] /\
//        // [E_ink <-> (not(Analog) /\ not(HighResolution) /\ Screen)]

//        BoolVar[] vars = new BoolVar[] {Analog, HighResolution, E_ink};
//        BoolVar boolVar = model.boolVar();
//        BoolVar equalVar = model.boolVar();
//        model.sum(vars, "!=", 1).reifyWith(boolVar);
//        model.sum(vars, "=", 1).reifyWith(equalVar);

        LogOp logOp = LogOp.ifOnlyIf(Screen, Analog);
//        LogOp negLopOp = LogOp.or(LogOp.and(Screen.not(), LogOp.or(Analog, HighResolution, E_ink)),
//                LogOp.and(Screen, boolVar));
        LogOp ctxLogOp = LogOp.implies(KB, logOp);

        model.addClauses(logOp);
//        print("alternative(Screen, Analog, HighResolution, E_ink)", 0);

//        startIdx = model.getNbCstrs();
//        model.addClauses(negLopOp);
//        print("not(alternative(Screen, Analog, HighResolution, E_ink))", startIdx);

//        for (int i = 0; i < model.getNbCstrs(); i++) {
//            model.unpost(model.getCstrs()[i]);
//        }
//        print("after unpost", 0);
//        List<Constraint> constraints = new ArrayList<>();
//        constraints.add(model.getCstrs()[1]);
//        for (int i = 0; i < model.getNbCstrs(); i++) {
//            model.unpost(model.getCstrs()[i]);
//        }
//        print("after unpost", 0);

        startIdx = model.getNbCstrs();
        model.addClauses(ctxLogOp);
//        print("KB -> alternative(Screen, Analog, HighResolution, E_ink)", 0);

//        for (int i = 0; i < model.getNbCstrs(); i++) {
//            constraints.add(model.getCstrs()[i]);
//        }

        // print constraints
//        System.out.println("Constraints");
//        for (int i = 0; i < constraints.size(); i++) {
//            System.out.println(constraints.get(i));
//        }

//        model.unpost(model.getCstrs());
//        print("all", 0);
//
//        model.post(constraints.toArray(new Constraint[0]));
//        print("all", 0);

        model.addClauseFalse(KB);
        model.addClauseTrue(Analog);

        // copy constraints to constraints
        List<Constraint> constraints = new ArrayList<>();
        for (int i = 0; i < model.getNbCstrs(); i++) {
            constraints.add(model.getCstrs()[i]);
        }

        // unpost all constraints
        model.unpost(model.getCstrs());

        // add constraints in the reverse order
        for (int i = constraints.size() - 1; i >= 0; i--) {
            model.post(constraints.get(i));
        }

        print("all", 0);

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
//            System.out.println("Smartwatch = " + Smartwatch.getValue());
//            System.out.println("Connector = " + Connector.getValue());
            System.out.println("Screen = " + Screen.getValue());
//            System.out.println("Camera = " + Camera.getValue());
//            System.out.println("Compass = " + Compass.getValue());
//            System.out.println("GPS = " + GPS.getValue());
//            System.out.println("Cellular = " + Cellular.getValue());
//            System.out.println("Wifi = " + Wifi.getValue());
//            System.out.println("Bluetooth = " + Bluetooth.getValue());
            System.out.println("Analog = " + Analog.getValue());
            System.out.println("HighResolution = " + HighResolution.getValue());
            System.out.println("E_ink = " + E_ink.getValue());
            System.out.println("KB = " + KB.getValue());
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
