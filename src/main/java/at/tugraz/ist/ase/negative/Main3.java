package at.tugraz.ist.ase.negative;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;

public class Main3 {
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

        // Smartwatch <-> Connector
        int startIdx = 0;
        LogOp logOp = LogOp.ifOnlyIf(Smartwatch, Connector);
        LogOp negLopOp = LogOp.nand(LogOp.implies(Smartwatch, Connector), LogOp.implies(Connector, Smartwatch));

        model.addClauses(logOp);
        print("Smartwatch <-> Connector", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(Smartwatch <-> Connector)", startIdx);

//        model.addClauseTrue(Smartwatch);
//        model.addClauseFalse(Connector);

        // Smartwatch <-> Screen
        startIdx = model.getNbCstrs();
        logOp = LogOp.ifOnlyIf(Smartwatch, Screen);
        negLopOp = LogOp.nand(LogOp.implies(Smartwatch, Screen), LogOp.implies(Screen, Smartwatch));

        model.addClauses(logOp);
        print("Smartwatch <-> Screen", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(Smartwatch <-> Screen)", startIdx);

        // Camera -> Smartwatch
        startIdx = model.getNbCstrs();
        logOp = LogOp.implies(Camera, Smartwatch);
        negLopOp = LogOp.and(Camera, Smartwatch.not());

        model.addClauses(logOp);
        print("Camera -> Smartwatch", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(Camera -> Smartwatch)", startIdx);

//        model.addClauseTrue(Camera);
//        model.addClauseFalse(Smartwatch);

        // Compass -> Smartwatch
        startIdx = model.getNbCstrs();
        logOp = LogOp.implies(Compass, Smartwatch);
        negLopOp = LogOp.and(Compass, Smartwatch.not());

        model.addClauses(logOp);
        print("Compass -> Smartwatch", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(Compass -> Smartwatch)", startIdx);

        // OR
        // Connector <-> (GPS \/ Cellular \/ Wifi \/ Bluetooth)
        startIdx = model.getNbCstrs();
        logOp = LogOp.ifOnlyIf(Connector, LogOp.or(GPS, Cellular, Wifi, Bluetooth));
        negLopOp = LogOp.nand(LogOp.implies(Connector, LogOp.or(GPS, Cellular, Wifi, Bluetooth)), LogOp.implies(LogOp.or(GPS, Cellular, Wifi, Bluetooth), Connector));

        model.addClauses(logOp);
        print("Connector <-> (GPS \\/ Cellular \\/ Wifi \\/ Bluetooth)", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(Connector <-> (GPS \\/ Cellular \\/ Wifi \\/ Bluetooth))", startIdx);

//        model.addClauseFalse(Connector);
//        model.addClauseTrue(GPS);
//        model.addClauseFalse(Cellular);
//        model.addClauseFalse(Wifi);
//        model.addClauseFalse(Bluetooth);

        // Alternative
        // [Analog <-> (not(HighResolution) /\ not(E_ink) /\ Screen)] /\
        // [HighResolution <-> (not(Analog) /\ not(E_ink) /\ Screen)] /\
        // [E_ink <-> (not(Analog) /\ not(HighResolution) /\ Screen)]
        startIdx = model.getNbCstrs();
        logOp = LogOp.and(LogOp.ifOnlyIf(Analog, LogOp.and(LogOp.nor(HighResolution, E_ink), Screen)),
                LogOp.ifOnlyIf(HighResolution, LogOp.and(LogOp.nor(Analog, E_ink), Screen)),
                LogOp.ifOnlyIf(E_ink, LogOp.and(LogOp.nor(Analog, HighResolution), Screen)));
        negLopOp = LogOp.nand(LogOp.ifOnlyIf(Analog, LogOp.and(LogOp.nor(HighResolution, E_ink), Screen)),
                LogOp.ifOnlyIf(HighResolution, LogOp.and(LogOp.nor(Analog, E_ink), Screen)),
                LogOp.ifOnlyIf(E_ink, LogOp.and(LogOp.nor(Analog, HighResolution), Screen)));

        model.addClauses(logOp);
        print("alternative(Screen, Analog, HighResolution, E_ink)", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(alternative(Screen, Analog, HighResolution, E_ink))", startIdx);

//        model.addClauseTrue(Screen);
//        model.addClauseTrue(Analog);
//        model.addClauseTrue(HighResolution);
//        model.addClauseFalse(E_ink);

        // Requires
        // Camera -> HighResolution
        startIdx = model.getNbCstrs();
        logOp = LogOp.implies(Camera, HighResolution);
        negLopOp = LogOp.and(Camera, HighResolution.not());

        model.addClauses(logOp);
        print("Camera -> HighResolution", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(Camera -> HighResolution)", startIdx);

        // Compass -> GPS
        startIdx = model.getNbCstrs();
        logOp = LogOp.implies(Compass, GPS);
        negLopOp = LogOp.and(Compass, GPS.not());

        model.addClauses(logOp);
        print("Compass -> GPS", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(Compass -> GPS)", startIdx);

        // Excludes
        // not(Cellular) \/ not(Analog)
        startIdx = model.getNbCstrs();
        logOp = LogOp.or(Cellular.not(), Analog.not());
        negLopOp = LogOp.nor(Cellular.not(), Analog.not());
        model.addClauses(logOp);
        print("not(Cellular) \\/ not(Analog)", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(not(Cellular) \\/ not(Analog))", startIdx);

//        model.addClauseTrue(Cellular);
//        model.addClauseFalse(Analog);

        // 3CNF
        // not(Analog) \/ Wifi \/ Cellular
        startIdx = model.getNbCstrs();
        logOp = LogOp.or(Analog.not(), Wifi, Cellular);
        negLopOp = LogOp.nor(Analog.not(), Wifi, Cellular);
        model.addClauses(logOp);
        print("not(Analog) \\/ Wifi \\/ Cellular", startIdx);

        startIdx = model.getNbCstrs();
        model.addClauses(negLopOp);
        print("not(not(Analog) \\/ Wifi \\/ Cellular)", startIdx);

//        model.addClauseTrue(Analog);
//        model.addClauseTrue(Wifi);
//        model.addClauseFalse(Cellular);

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
