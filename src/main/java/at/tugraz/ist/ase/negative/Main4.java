package at.tugraz.ist.ase.negative;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;

/**
 * Test with 26 features in an alternative relationship.
 */
public class Main4 {
    static Model model;
    static BoolVar Smartwatch;
    static BoolVar F1;
    static BoolVar F2;
    static BoolVar F3;
    static BoolVar F4;
    static BoolVar F5;
    static BoolVar F6;
    static BoolVar F7;
    static BoolVar F8;
    static BoolVar F9;
    static BoolVar F10;
    static BoolVar F11;
    static BoolVar F12;
    static BoolVar F13;
    static BoolVar F14;
    static BoolVar F15;
    static BoolVar F16;
    static BoolVar F17;
    static BoolVar F18;
    static BoolVar F19;
    static BoolVar F20;
    static BoolVar F21;
    static BoolVar F22;
    static BoolVar F23;
    static BoolVar F24;
    static BoolVar F25;
    static BoolVar F26;

    public static void main(String[] args) {
        model = new Model();
        Smartwatch = model.boolVar("Smartwatch");
        F1 = model.boolVar("F1");
        F2 = model.boolVar("F2");
        F3 = model.boolVar("F3");
        F4 = model.boolVar("F4");
        F5 = model.boolVar("F5");
        F6 = model.boolVar("F6");
        F7 = model.boolVar("F7");
        F8 = model.boolVar("F8");
        F9 = model.boolVar("F9");
        F10 = model.boolVar("F10");
        F11 = model.boolVar("F11");
        F12 = model.boolVar("F12");
        F13 = model.boolVar("F13");
        F14 = model.boolVar("F14");
        F15 = model.boolVar("F15");
        F16 = model.boolVar("F16");
        F17 = model.boolVar("F17");
        F18 = model.boolVar("F18");
        F19 = model.boolVar("F19");
        F20 = model.boolVar("F20");
        F21 = model.boolVar("F21");
        F22 = model.boolVar("F22");
        F23 = model.boolVar("F23");
        F24 = model.boolVar("F24");
        F25 = model.boolVar("F25");
        F26 = model.boolVar("F26");

        // Alternative relationship
        int startIdx = 0;
        // Alternative
        // [F1 <-> (not(F2) /\ not(F3) /\ ... /\ Smartwatch)] /\
        // [F2 <-> (not(F1) /\ not(F3) /\ ... /\ Smartwatch)] /\
        // ...
        // [F26 <-> (not(F1) /\ not(F2) /\ ... /\ Smartwatch)]
        BoolVar[] vars = new BoolVar[]{F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26};
        BoolVar equalVar = model.boolVar();
        BoolVar notEqualVar = model.boolVar();
        model.sum(vars, "=", 1).reifyWith(equalVar);
        model.sum(vars, "!=", 1).reifyWith(notEqualVar);

        LogOp logOp = LogOp.or(LogOp.and(Smartwatch.not(), F1.not(), F2.not(), F3.not(), F4.not(), F5.not(), F6.not(), F7.not(), F8.not(), F9.not(), F10.not(), F11.not(), F12.not(), F13.not(), F14.not(), F15.not(), F16.not(), F17.not(), F18.not(), F19.not(), F20.not(), F21.not(), F22.not(), F23.not(), F24.not(), F25.not(), F26.not()),
                LogOp.and(Smartwatch, equalVar));
        LogOp negLopOp = LogOp.or(LogOp.and(Smartwatch.not(), LogOp.or(F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26),
                LogOp.and(Smartwatch, notEqualVar)));

        model.addClauses(logOp);
        print("alternative(Screen, Analog, HighResolution, E_ink)", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(alternative(Screen, Analog, HighResolution, E_ink))", startIdx);

//        model.addClauseTrue(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseFalse(E_ink);

        solve();
    }

    private static void solve() {
        if (model.getSolver().solve()) {
            System.out.println("Smartwatch = " + Smartwatch.getValue());
            System.out.println("F1 = " + F1.getValue());
            System.out.println("F2 = " + F2.getValue());
            System.out.println("F3 = " + F3.getValue());
            System.out.println("F4 = " + F4.getValue());
            System.out.println("F5 = " + F5.getValue());
            System.out.println("F6 = " + F6.getValue());
            System.out.println("F7 = " + F7.getValue());
            System.out.println("F8 = " + F8.getValue());
            System.out.println("F9 = " + F9.getValue());
            System.out.println("F10 = " + F10.getValue());
            System.out.println("F11 = " + F11.getValue());
            System.out.println("F12 = " + F12.getValue());
            System.out.println("F13 = " + F13.getValue());
            System.out.println("F14 = " + F14.getValue());
            System.out.println("F15 = " + F15.getValue());
            System.out.println("F16 = " + F16.getValue());
            System.out.println("F17 = " + F17.getValue());
            System.out.println("F18 = " + F18.getValue());
            System.out.println("F19 = " + F19.getValue());
            System.out.println("F20 = " + F20.getValue());
            System.out.println("F21 = " + F21.getValue());
            System.out.println("F22 = " + F22.getValue());
            System.out.println("F23 = " + F23.getValue());
            System.out.println("F24 = " + F24.getValue());
            System.out.println("F25 = " + F25.getValue());
            System.out.println("F26 = " + F26.getValue());
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
