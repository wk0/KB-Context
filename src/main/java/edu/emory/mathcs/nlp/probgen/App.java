package edu.emory.mathcs.nlp.probgen;
import org.apache.jena.rdf.model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.emory.mathcs.nlp.probgen.InferenceModel.dbpediaInfModel;
import static edu.emory.mathcs.nlp.probgen.InferenceModel.typeData;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting!");

        LoadLogger logger = new LoadLogger();
        logger.loadLogger();
        InferenceModel inf = new InferenceModel();
        inf.makeInferenceModel();

        Resource usa = dbpediaInfModel.getResource("http://dbpedia.org/resource/M1917_Enfield");

        StmtIterator iter = usa.listProperties();

        while (iter.hasNext()){
            Statement statement = iter.nextStatement();
            Resource  subject   = statement.getSubject();     // get the subject
            Property  predicate = statement.getPredicate();   // get the predicate
            RDFNode   object    = statement.getObject();      // get the object

            String type = "#type";
            Pattern typePattern = Pattern.compile(type);
            Matcher typeMatcher = typePattern.matcher(predicate.toString());

            String dbo = "dbpedia.org/ontology";
            Pattern dboPattern = Pattern.compile(dbo);
            Matcher dboMatcher = dboPattern.matcher(object.toString());

            if (typeMatcher.find() && dboMatcher.find() && object instanceof Resource) {
                System.out.println("Subject: " + subject.toString());
                System.out.println("Predicate: " + predicate.toString());
                System.out.println("Object:" + object.toString());
            } else {

            }
        }
    }
}