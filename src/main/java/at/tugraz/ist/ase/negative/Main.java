package at.tugraz.ist.ase.negative;

import at.tugraz.ist.ase.fm.core.*;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.fm.parser.SXFMParser;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.Variable;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Test for feature models
 * Can get opposite of a constraint
 *
 * ARITHM ([A + not(X) >= 1])
 * ARITHM ([X + not(A) >= 1])
 *
 * Opposite:
 * ARITHM ([A + X >= 1])
 * ARITHM ([not(A) + not(X) >= 1])
 *
 */
public class Main {
    private static Model model;
    private static FeatureModel fm;

    private static final List<BoolVar> variables = new LinkedList<>();

    public static void main(String[] args) throws FeatureModelParserException, FeatureModelException {
        File file = new File("src/main/resources/FM_10_0.splx");
        SXFMParser parser = new SXFMParser();
        fm = parser.parse(file);

        System.out.println(fm.toString());

        model = new Model(fm.getName());

        createVariables();
        createConstraints();

        for (Constraint cstr : model.getCstrs()) {
            System.out.println(cstr.toString());
        }
    }

    private static void createVariables() throws FeatureModelException {
        for (int i = 0; i < fm.getNumOfFeatures(); i++) {
            BoolVar var = model.boolVar(fm.getFeature(i).getName());

            variables.add(var);
        }

//        BoolVar[] x = new BoolVar[featureModel.getNumOfFeatures()];
//        for (int i = 0; i < featureModel.getNumOfFeatures(); i++) {
//            x[i] = model.boolVar(featureModel.getFeature(i).getName());
//        }
//
//        // set variables variableList
//        List<String> variables = new ArrayList<>();
//        for (Variable v: model.getVars()) {
//            variables.add(v.getName());
//        }
//        this.variables = new LinkedHashSet<>(variables);
//        this.setVariables(variables);
    }

    /**
     * On the basic of a given {@link FeatureModel}, this function creates
     * corresponding constraints for the model.
     */
    private static void createConstraints() throws IllegalArgumentException {
        // first convert relationships into constraints
        for (Relationship relationship: fm.getRelationships()) {
            BasicRelationship basicRelationship = (BasicRelationship) relationship;
            BoolVar leftVar = getVarWithName(basicRelationship.getLeftSide().getName());
            BoolVar rightVar;

            int oldNumCstrs = model.getNbCstrs();
            int newNumCstrs = oldNumCstrs;
            LogOp op;
            LogOp notOp;
            switch (relationship.getType())
            {
                case MANDATORY:
                    rightVar = getVarWithName(basicRelationship.getRightSide().get(0).getName());
                    // leftVar <=> rightVar
                    op = LogOp.ifOnlyIf(leftVar, rightVar);
                    model.addClauses(op);

                    notOp = LogOp.nor(op);
                    model.addClauses(notOp);

                    newNumCstrs = model.getNbCstrs();
                    break;
                case OPTIONAL:
                    rightVar = getVarWithName(basicRelationship.getRightSide().get(0).getName());
                    // leftVar => rightVar
                    op = LogOp.implies(leftVar, rightVar);
                    model.addClauses(op);

                    notOp = LogOp.nor(op);
                    model.addClauses(notOp);

                    newNumCstrs = model.getNbCstrs();
                    break;
                case OR:
                    // LogOp of rule {A \/ B \/ ... \/ C}
                    LogOp rightLogOp = getRightSideOfOrRelationship(basicRelationship.getRightSide());
                    // leftVar <=> rightLogOp
                    op = LogOp.ifOnlyIf(leftVar, rightLogOp);
                    model.addClauses(op);

                    notOp = LogOp.nor(op);
                    model.addClauses(notOp);

                    newNumCstrs = model.getNbCstrs();
                    break;
                case ALTERNATIVE:
                    // LogOp of an ALTERNATIVE relationship
                    op = getLogOpOfAlternativeRelationship(relationship);
                    model.addClauses(op);

                    notOp = LogOp.nor(op);
                    model.addClauses(notOp);

                    newNumCstrs = model.getNbCstrs();
                    break;
            }

            setConstraintsToRelationship(oldNumCstrs, newNumCstrs, relationship);
        }

        // second convert constraints of {@link FeatureModel} into ChocoSolver constraints
        for (Relationship relationship: fm.getConstraints()) {
            if (relationship.isType(RelationshipType.ThreeCNF)) {
                ThreeCNFConstraint threeCNFConstraint = (ThreeCNFConstraint) relationship;

                int oldNumCstrs = model.getNbCstrs();
                int newNumCstrs = oldNumCstrs;

                LogOp op = LogOp.or();
                LogOp notOp;
                for (Clause clause: threeCNFConstraint.getClauses()) {
                    boolean value = Boolean.parseBoolean(clause.getLiteral());
                    BoolVar var = getVarWithName(clause.getLiteral());

                    if (value) {
                        op.addChild(var);
                    } else {
                        op.addChild(LogOp.nor(var));
                    }
                }
                model.addClauses(op);
                newNumCstrs = model.getNbCstrs();

                notOp = LogOp.nor(op);
                model.addClauses(notOp);

                setConstraintsToRelationship(oldNumCstrs, newNumCstrs, relationship);
            } else {
                BasicRelationship basicRelationship = (BasicRelationship) relationship;

                BoolVar leftVar = getVarWithName(basicRelationship.getLeftSide().getName());
                BoolVar rightVar = getVarWithName(basicRelationship.getRightSide().get(0).getName());

                int oldNumCstrs = model.getNbCstrs();
                int newNumCstrs = oldNumCstrs;
                LogOp op;
                LogOp notOp;
                switch (relationship.getType()) {
                    case REQUIRES:
                        op = LogOp.implies(leftVar, rightVar);
                        model.addClauses(op);

                        notOp = LogOp.nor(op);
                        model.addClauses(notOp);

                        newNumCstrs = model.getNbCstrs();
                        break;
                    case EXCLUDES:
                        op = LogOp.or(LogOp.nor(leftVar), LogOp.nor(rightVar));
                        model.addClauses(op);

                        notOp = LogOp.nor(op);
                        model.addClauses(notOp);

                        newNumCstrs = model.getNbCstrs();
                        break;
                }

                setConstraintsToRelationship(oldNumCstrs, newNumCstrs, relationship);
            }
        }
    }

    /**
     * Add back the created constraints to the {@link Relationship}.
     * It means that the {@link Relationship} holds references to the constraint in the ChocoSolver model.
     * This allows us to reuse the constraints without recreating.
     *
     * @param oldNumCstrs - the number of old constraints
     * @param newNumCstrs - the number of all constraints
     * @param relationship - a {@link Relationship}
     */
    private static void setConstraintsToRelationship(int oldNumCstrs, int newNumCstrs, Relationship relationship) {
        Constraint[] constraints = model.getCstrs();
        for (int i = oldNumCstrs; i < newNumCstrs; i++) {
            relationship.setConstraint(constraints[i].toString());
        }

//        Constraint[] constraints = model.getCstrs();
//        for (int i = 0; i < constraints.length; i++) { // TODO - buon cuoi qua di
//            if (oldNumCstrs != newNumCstrs && i >= oldNumCstrs && i < newNumCstrs) {
//                relationship.setConstraint(constraints[i].toString()); // TODO - code tao lao
//            }
//        }
    }

    /**
     * Create a {@link LogOp} that represent to an ALTERNATIVE relationship.
     * The form of rule is {C1 <=> (not C2 /\ ... /\ not Cn /\ P) /\
     *                      C2 <=> (not C1 /\ ... /\ not Cn /\ P) /\
     *                      ... /\
     *                      Cn <=> (not C1 /\ ... /\ not Cn-1 /\ P)
     *
     * @param relationship - a {@link Relationship} of {@link FeatureModel}
     * @return A {@link LogOp} that represent to an ALTERNATIVE relationship
     * @throws IllegalArgumentException when couldn't find the corresponding variable in the model
     */
    private static LogOp getLogOpOfAlternativeRelationship(Relationship relationship) throws IllegalArgumentException {
        BasicRelationship basicRelationship = (BasicRelationship) relationship;
        LogOp op = LogOp.and(); // an LogOp of AND operators
        for (int i = 0; i < basicRelationship.getRightSide().size(); i++) {
            BoolVar rightVar = getVarWithName(basicRelationship.getRightSide().get(i).getName());
            // (not C2 /\ ... /\ not Cn /\ P)
            LogOp rightSide = getRightSideOfAlternativeRelationship(basicRelationship.getLeftSide().getName(), basicRelationship.getRightSide(), i);
            // {C1 <=> (not C2 /\ ... /\ not Cn /\ P)}
            LogOp part = LogOp.ifOnlyIf(rightVar, rightSide);
            op.addChild(part);
        }
        return op;
    }

    /**
     * Create a {@link LogOp} that represent the rule {(not C2 /\ ... /\ not Cn /\ P)}.
     * This is the right side of the rule {C1 <=> (not C2 /\ ... /\ not Cn /\ P)}
     *
     * @param leftSide - the name of the parent feature
     * @param rightSide - names of the child features
     * @param removedIndex - the index of the child feature that is the left side of the rule
     * @return a {@link LogOp} that represent the rule {(not C2 /\ ... /\ not Cn /\ P)}.
     * @throws IllegalArgumentException when couldn't find the variable in the model
     */
    private static LogOp getRightSideOfAlternativeRelationship(String leftSide, List<Feature> rightSide, int removedIndex) throws IllegalArgumentException {
        BoolVar leftVar = getVarWithName(leftSide);
        LogOp op = LogOp.and(leftVar);
        for (int i = 0; i < rightSide.size(); i++) {
            if (i != removedIndex) {
                op.addChild(LogOp.nor(getVarWithName(rightSide.get(i).getName())));
            }
        }
        return op;
    }

    /**
     * Create a {@link LogOp} for the right side of an OR relationship.
     * The form of rule is {A \/ B \/ ... \/ C}.
     *
     * @param rightSide - an array of feature names which belong to the right side of an OR relationship
     * @return a {@link LogOp} or null if the rightSide is empty
     * @throws IllegalArgumentException when couldn't find a variable which corresponds to the given feature name
     */
    private static LogOp getRightSideOfOrRelationship(List<Feature> rightSide) throws IllegalArgumentException {
        if (rightSide.size() == 0) return null;
        LogOp op = LogOp.or(); // create a LogOp of OR operators
        for (Feature s : rightSide) {
            BoolVar var = getVarWithName(s.getName());
            op.addChild(var);
        }
        return op;
    }

    /**
     * On the basic of a feature name, this function return
     * the corresponding ChocoSolver variable in the model.
     *
     * @param name - a feature name
     * @return the corresponding ChocoSolver variable in the model or null
     * @throws IllegalArgumentException when couldn't find the variable in the model
     */
    public static BoolVar getVarWithName(String name) throws IllegalArgumentException {
        Variable var = null;
        for (Variable v : model.getVars()) {
            if (v.getName().equals(name)) {
                var = v;
                break;
            }
        }
        if (var == null)
            throw new IllegalArgumentException("The feature " + name + " is not exist in the feature model!");
        return (BoolVar) var;
    }
}
