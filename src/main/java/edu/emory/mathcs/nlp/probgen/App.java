package edu.emory.mathcs.nlp.probgen;
import org.apache.jena.rdf.model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.emory.mathcs.nlp.probgen.InferenceModel.dbpediaInfModel;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting!");

        LoadLogger logger = new LoadLogger();
        logger.loadLogger();
        InferenceModel inf = new InferenceModel();
        inf.makeInferenceModel();

        Resource resource = dbpediaInfModel.getResource("http://dbpedia.org/resource/M1917_Enfield");

        StmtIterator iter = resource.listProperties();

        while (iter.hasNext()){
            Statement statement = iter.nextStatement();
            Resource  subject   = statement.getSubject();     // get the subject
            Property  predicate = statement.getPredicate();   // get the predicate
            RDFNode   object    = statement.getObject();      // get the object

            if (checkType(predicate) && checkDBO(object) && object instanceof Resource) {
                System.out.println("Subject type");
                System.out.println("Object:" + object.toString());
            } else {

            }
        }

    }

    public static boolean checkType(Property predicate) {
        String type = "#type";
        Pattern typePattern = Pattern.compile(type);
        Matcher typeMatcher = typePattern.matcher(predicate.toString());
        return typeMatcher.find();
    }

    public static boolean checkDBO(RDFNode object) {
        String dbo = "dbpedia.org/ontology";
        Pattern dboPattern = Pattern.compile(dbo);
        Matcher dboMatcher = dboPattern.matcher(object.toString());
        return dboMatcher.find();
    }
}