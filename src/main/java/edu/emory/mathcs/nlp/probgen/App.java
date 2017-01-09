package edu.emory.mathcs.nlp.probgen;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;
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

        Resource resource = dbpediaInfModel.getResource("http://dbpedia.org/resource/Barack_Obama");

        List<String> resourceTypeList = getResourceTypeList(resource);
        System.out.println(resourceTypeList);
        List<String> ontoloyList = getOntoloyList(resource);
        System.out.println(ontoloyList);
        for (String o: ontoloyList) {
            System.out.println(o);
        }

    }

    static ArrayList getOntoloyList(Resource r) {
        ArrayList<String> ontoloyArray = new ArrayList<String>();
        StmtIterator oIter = r.listProperties();
        while (oIter.hasNext()){
            Statement statement = oIter.nextStatement();
            Property  predicate = statement.getPredicate();     // get the predicate

            if (checkDBO(predicate.toString())) {
                ontoloyArray.add(predicate.toString());
            }
        }
        return ontoloyArray;
    }

    static ArrayList getResourceTypeList(Resource r) {
        ArrayList<String> resourceTypeArray = new ArrayList<String>();
        StmtIterator rIter = r.listProperties();
        while (rIter.hasNext()){
            Statement statement = rIter.nextStatement();
//            Resource  subject   = statement.getSubject();       // get the subject
            Property  predicate = statement.getPredicate();     // get the predicate
            RDFNode   object    = statement.getObject();        // get the object

            if (checkType(predicate.toString()) && checkDBO(object.toString()) && object instanceof Resource) {
                resourceTypeArray.add(object.toString());
            }
        }
        return resourceTypeArray;
    }

    public static boolean checkType(String predicate) {
        String type = "#type";
        Pattern typePattern = Pattern.compile(type);
        Matcher typeMatcher = typePattern.matcher(predicate);
        return typeMatcher.find();
    }

    public static boolean checkDBO(String object) {
        String dbo = "dbpedia.org/ontology";
        Pattern dboPattern = Pattern.compile(dbo);
        Matcher dboMatcher = dboPattern.matcher(object);
        return dboMatcher.find();
    }
}