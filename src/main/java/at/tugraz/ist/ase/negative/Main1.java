package at.tugraz.ist.ase.negative;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

/**
 * Test for Renault's constraints.
 * Can get opposite of a constraint
 */
public class Main1 {
    public static void main(String[] args) {
        Model model = new Model();

        BoolVar x = model.boolVar("x");
        BoolVar y = model.boolVar("y");
        BoolVar z = model.boolVar("z");

        Constraint c1 = model.and(x, y, z);
        Constraint c2 = model.and(x, y.not(), z);
        Constraint c3 = model.and(x, y, z.not());
        Constraint c4 = model.and(x, y.not(), z.not());

        Constraint c5 = model.or(c1, c2, c3, c4);

        c5.post();

        // can get opposite
        Constraint c6 = c5.getOpposite();
        c6.post();
//        model.unpost(c5);

        for (Constraint cstr : model.getCstrs()) {
            System.out.println(cstr.getName() + " " + cstr);
        }

        model.getSolver().solve();

        System.out.println("x: " + x.getValue());
        System.out.println("y: " + y.getValue());
        System.out.println("z: " + z.getValue());
    }
}
