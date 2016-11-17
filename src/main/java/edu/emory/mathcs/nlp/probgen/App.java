package edu.emory.mathcs.nlp.probgen;

import org.apache.jena.rdf.model.*;
import org.apache.jena.datatypes.RDFDatatype;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static edu.emory.mathcs.nlp.probgen.InferenceModel.dbpediaInfModel;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting!");

        LoadLogger logger = new LoadLogger();
        logger.loadLogger();
        InferenceModel inf = new InferenceModel();
        inf.makeInferenceModel();
        
}