package at.tugraz.ist.ase.negative;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 * Test for x = a -> y = b
 */
public class Main2 {
    public static void main(String[] args) {
        Model model = new Model();

        IntVar x = model.intVar("x", 0, 10);
        IntVar y = model.intVar("y", 0, 10);
        IntVar z = model.intVar("z", 0, 10);

        model.ifThen(model.arithm(x, "=", 3), model.arithm(y, "=", 3));

        Constraint c = model.getCstrs()[model.getNbCstrs() - 1].getOpposite();
        c.post();

        model.ifThen(model.arithm(x, "=", 3), model.arithm(z, "=", 2));

        c = model.getCstrs()[model.getNbCstrs() - 1].getOpposite();
        c.post();

//        Constraint c6 = c5.getOpposite();
//        c6.post();
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
