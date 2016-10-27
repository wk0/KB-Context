package edu.emory.mathcs.nlp.probgen;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.reasoner.ValidityReport;

import java.util.Iterator;

/**
 * Created by gary on 10/27/16.
 */
public class ModelValidator {

    public static void validateModel(InfModel m) {
        ValidityReport validity = m.validate();
        if (validity.isValid()) {
            System.out.println("OK, valid.");
            return;
        } else {
            System.out.println("Conflicts");
            for (Iterator i = validity.getReports(); i.hasNext(); ) {
                System.out.println(" - " + i.next());
            }
            return;
        }
    }
}
