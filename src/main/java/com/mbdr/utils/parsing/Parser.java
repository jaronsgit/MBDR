package com.mbdr.utils.parsing;

import java.io.IOException;
import java.util.ArrayList;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class Parser {

    public final static String TWIDDLE = "|~";

    public static boolean isDefeasible(String formula){
        return formula.contains(TWIDDLE);
    }

    public static KnowledgeBase parseFormulas(ArrayList<String> formulas) throws ParserException, IOException{
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        for(String rawFormula : formulas){
            // Defeasible implication
            if(isDefeasible(rawFormula)){
                PlFormula formula = parseDefeasibleFormula(rawFormula);
                knowledgeBase.addDefeasibleFormula(formula);
            }
            // Propositional formula
            else{
                PlFormula formula = parsePropositionalFormula(rawFormula);
                knowledgeBase.addPropositionalFormula(formula);
            }
        }
        return knowledgeBase;
    }

    public static PlFormula parsePropositionalFormula(String propositionalFormula) throws ParserException, IOException{
        PlParser parser = new PlParser();
        return parser.parseFormula(propositionalFormula);
    }

    public static PlFormula parseDefeasibleFormula(String defeasibleFormula) throws ParserException, IOException{
        PlParser parser = new PlParser();
        if(!isDefeasible(defeasibleFormula)){
            throw new ParserException("Expected a defeasible formula");
        }
        return parser.parseFormula(materialiseDefeasibleImplication(defeasibleFormula));
    }

    /*
     * The BNF for a propositional belief set is given by (starting symbol is
     * FORMULASET)
     * FORMULASET ::== FORMULA ( "\n" FORMULA )*
     * FORMULA ::== PROPOSITION | "(" FORMULA ")" | FORMULA ">>" FORMULA | FORMULA
     * "||" FORMULA | FORMULA "=>" FORMULA | FORMULA "<=>" FORMULA | FORMULA "^^"
     * FORMULA | "!" FORMULA | "+" | "-"
     */

    /**
     * Materialises given defeasible implication (changes twiddle to material
     * implication)
     * 
     * @param DI
     * @return
     */
    public static String materialiseDefeasibleImplication(String DI) {
        return DI.replace(TWIDDLE, "=>");
    }

    // /**
    //  * Forms the union of two knowledge bases
    //  * 
    //  * @param A
    //  * @param B
    //  * @return
    //  */
    // public static PlBeliefSet Union(PlBeliefSet A, PlBeliefSet B) {
    //     PlBeliefSet temp = new PlBeliefSet();
    //     temp.addAll(A);
    //     temp.addAll(B);
    //     return temp;
    // }

}
